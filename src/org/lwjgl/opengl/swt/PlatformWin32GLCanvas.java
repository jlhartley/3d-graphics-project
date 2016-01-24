package org.lwjgl.opengl.swt;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.ARBRobustness;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.NVMultisampleCoverage;
import org.lwjgl.opengl.WGL;
import org.lwjgl.opengl.WGLARBContextFlushControl;
import org.lwjgl.opengl.WGLARBCreateContext;
import org.lwjgl.opengl.WGLARBCreateContextProfile;
import org.lwjgl.opengl.WGLARBCreateContextRobustness;
import org.lwjgl.opengl.WGLARBMultisample;
import org.lwjgl.opengl.WGLARBPixelFormat;
import org.lwjgl.opengl.WGLARBPixelFormatFloat;
import org.lwjgl.opengl.WGLARBRobustnessApplicationIsolation;
import org.lwjgl.opengl.WGLEXTCreateContextES2Profile;
import org.lwjgl.opengl.WGLEXTFramebufferSRGB;
import org.lwjgl.opengl.WGLNVMultisampleCoverage;
import org.lwjgl.opengl.swt.GLData.API;
import org.lwjgl.opengl.swt.GLData.Profile;
import org.lwjgl.opengl.swt.GLData.ReleaseBehavior;
import org.lwjgl.system.APIBuffer;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.APIUtil.APIVersion;
import org.lwjgl.system.Checks;
import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.windows.GDI32;
import org.lwjgl.system.windows.PIXELFORMATDESCRIPTOR;
import org.lwjgl.system.windows.User32;

/**
 * Windows-specific implementation of methods for GLCanvas.
 * 
 * @author Kai Burjack
 */
class PlatformWin32GLCanvas implements PlatformGLCanvas {
    private static final String USE_OWNDC_KEY = "org.eclipse.swt.internal.win32.useOwnDC";

    private static final WGL wgl;
    private static final FunctionProvider gl;

    static {
        // Cache the WGL instance
        wgl = new WGL(gl = GL.getFunctionProvider());
    }

    private long wglDelayBeforeSwapNVAddr = 0L;
    private boolean wglDelayBeforeSwapNVAddr_set = false;

    private static boolean atLeast32(int major, int minor) {
        return major == 3 && minor >= 2 || major > 3;
    }

    private static boolean atLeast30(int major, int minor) {
        return major == 3 && minor >= 0 || major > 3;
    }

    private static boolean validVersionGL(int major, int minor) {
        return (major == 0 && minor == 0) || // unspecified gets highest supported version on Nvidia
               (major >= 1 && minor >= 0) &&
               (major != 1 || minor <= 5) &&
               (major != 2 || minor <= 1) &&
               (major != 3 || minor <= 3) &&
               (major != 4 || minor <= 5);
    }

    private static boolean validVersionGLES(int major, int minor) {
        return (major == 0 && minor == 0) || // unspecified gets 1.1 on Nvidia
               (major >= 1 && minor >= 0) &&
               (major != 1 || minor <= 1) &&
               (major != 2 || minor <= 0);
    }

    /**
     * Validate the given {@link GLData} and throw an exception on validation error.
     * 
     * @param attribs
     *            the {@link GLData} to validate
     */
    public static void validateAttributes(GLData attribs) {
        if (attribs.alphaSize < 0) {
            throw new IllegalArgumentException("Alpha bits cannot be less than 0");
        }
        if (attribs.redSize < 0) {
            throw new IllegalArgumentException("Red bits cannot be less than 0");
        }
        if (attribs.greenSize < 0) {
            throw new IllegalArgumentException("Green bits cannot be less than 0");
        }
        if (attribs.blueSize < 0) {
            throw new IllegalArgumentException("Blue bits cannot be less than 0");
        }
        if (attribs.stencilSize < 0) {
            throw new IllegalArgumentException("Stencil bits cannot be less than 0");
        }
        if (attribs.depthSize < 0) {
            throw new IllegalArgumentException("Depth bits cannot be less than 0");
        }
        if (attribs.forwardCompatible && !atLeast30(attribs.majorVersion, attribs.minorVersion)) {
            throw new IllegalArgumentException("Forward-compatibility is only defined for OpenGL version 3.0 and above");
        }
        if (attribs.samples < 0) {
            throw new IllegalArgumentException("Invalid samples count");
        }
        if (attribs.profile != null && !atLeast32(attribs.majorVersion, attribs.minorVersion)) {
            throw new IllegalArgumentException("Context profiles are only defined for OpenGL version 3.2 and above");
        }
        if (attribs.api == null) {
            throw new IllegalArgumentException("Unspecified client API");
        }
        if (attribs.api == API.GL && !validVersionGL(attribs.majorVersion, attribs.minorVersion)) {
            throw new IllegalArgumentException("Invalid OpenGL version");
        }
        if (attribs.api == API.GLES && !validVersionGLES(attribs.majorVersion, attribs.minorVersion)) {
            throw new IllegalArgumentException("Invalid OpenGL ES version");
        }
        if (!attribs.doubleBuffer && attribs.swapInterval != null) {
            throw new IllegalArgumentException("Swap interval set but not using double buffering");
        }
        if (attribs.colorSamplesNV < 0) {
            throw new IllegalArgumentException("Invalid color samples count");
        }
        if (attribs.colorSamplesNV > attribs.samples) {
            throw new IllegalArgumentException("Color samples greater than number of (coverage) samples");
        }
        if (attribs.swapGroupNV < 0) {
            throw new IllegalArgumentException("Invalid swap group");
        }
        if (attribs.swapBarrierNV < 0) {
            throw new IllegalArgumentException("Invalid swap barrier");
        }
        if ((attribs.swapGroupNV > 0 || attribs.swapBarrierNV > 0) && !attribs.doubleBuffer) {
            throw new IllegalArgumentException("Swap group or barrier requested but not using double buffering");
        }
        if (attribs.swapBarrierNV > 0 && attribs.swapGroupNV == 0) {
            throw new IllegalArgumentException("Swap barrier requested but no valid swap group set");
        }
        if (attribs.loseContextOnReset && !attribs.robustness) {
            throw new IllegalArgumentException("Lose context notification requested but not using robustness");
        }
        if (attribs.contextResetIsolation && !attribs.robustness) {
            throw new IllegalArgumentException("Context reset isolation requested but not using robustness");
        }
    }

    /**
     * Encode the pixel format attributes stored in the given {@link GLData} into the given {@link IntBuffer} for wglChoosePixelFormatARB to
     * consume.
     */
    private void encodePixelFormatAttribs(IntBuffer ib, GLData attribs) {
        ib.put(WGLARBPixelFormat.WGL_DRAW_TO_WINDOW_ARB).put(1);
        ib.put(WGLARBPixelFormat.WGL_SUPPORT_OPENGL_ARB).put(1);
        ib.put(WGLARBPixelFormat.WGL_ACCELERATION_ARB).put(WGLARBPixelFormat.WGL_FULL_ACCELERATION_ARB);
        if (attribs.doubleBuffer)
            ib.put(WGLARBPixelFormat.WGL_DOUBLE_BUFFER_ARB).put(1);
        if (attribs.pixelFormatFloat)
            ib.put(WGLARBPixelFormat.WGL_PIXEL_TYPE_ARB).put(WGLARBPixelFormatFloat.WGL_TYPE_RGBA_FLOAT_ARB);
        else
            ib.put(WGLARBPixelFormat.WGL_PIXEL_TYPE_ARB).put(WGLARBPixelFormat.WGL_TYPE_RGBA_ARB);
        if (attribs.redSize > 0)
            ib.put(WGLARBPixelFormat.WGL_RED_BITS_ARB).put(attribs.redSize);
        if (attribs.greenSize > 0)
            ib.put(WGLARBPixelFormat.WGL_GREEN_BITS_ARB).put(attribs.greenSize);
        if (attribs.blueSize > 0)
            ib.put(WGLARBPixelFormat.WGL_BLUE_BITS_ARB).put(attribs.blueSize);
        if (attribs.alphaSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ALPHA_BITS_ARB).put(attribs.alphaSize);
        if (attribs.depthSize > 0)
            ib.put(WGLARBPixelFormat.WGL_DEPTH_BITS_ARB).put(attribs.depthSize);
        if (attribs.stencilSize > 0)
            ib.put(WGLARBPixelFormat.WGL_STENCIL_BITS_ARB).put(attribs.stencilSize);
        if (attribs.accumRedSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ACCUM_RED_BITS_ARB).put(attribs.accumRedSize);
        if (attribs.accumGreenSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ACCUM_GREEN_BITS_ARB).put(attribs.accumGreenSize);
        if (attribs.accumBlueSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ACCUM_BLUE_BITS_ARB).put(attribs.accumBlueSize);
        if (attribs.accumAlphaSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ACCUM_ALPHA_BITS_ARB).put(attribs.accumAlphaSize);
        if (attribs.accumRedSize > 0 || attribs.accumGreenSize > 0 || attribs.accumBlueSize > 0 || attribs.accumAlphaSize > 0)
            ib.put(WGLARBPixelFormat.WGL_ACCUM_BITS_ARB).put(attribs.accumRedSize + attribs.accumGreenSize + attribs.accumBlueSize + attribs.accumAlphaSize);
        if (attribs.sRGB)
            ib.put(WGLEXTFramebufferSRGB.WGL_FRAMEBUFFER_SRGB_CAPABLE_EXT).put(1);
        if (attribs.samples > 0) {
            ib.put(WGLARBMultisample.WGL_SAMPLE_BUFFERS_ARB).put(1);
            ib.put(WGLARBMultisample.WGL_SAMPLES_ARB).put(attribs.samples);
            if (attribs.colorSamplesNV > 0) {
                ib.put(WGLNVMultisampleCoverage.WGL_COLOR_SAMPLES_NV).put(attribs.colorSamplesNV);
            }
        }
        ib.put(0);
    }

    public int checkStyle(Composite parent, int style) {
        // Somehow we need to temporarily set 'org.eclipse.swt.internal.win32.useOwnDC'
        // to true or else context creation on Windows fails...
        if (parent != null) {
            if (!org.eclipse.swt.internal.win32.OS.IsWinCE
                    && org.eclipse.swt.internal.win32.OS.WIN32_VERSION >= org.eclipse.swt.internal.win32.OS.VERSION(6, 0)) {
                parent.getDisplay().setData(USE_OWNDC_KEY, Boolean.TRUE);
            }
        }
        return style;
    }

    public void resetStyle(Composite parent) {
        parent.getDisplay().setData(USE_OWNDC_KEY, Boolean.FALSE);
    }

    public long create(GLCanvas canvas, GLData attribs, GLData effective) {
        Canvas dummycanvas = new Canvas(canvas.getParent(), checkStyle(canvas.getParent(), canvas.getStyle()));
        long context = 0L;
        try {
            context = create(canvas.handle, dummycanvas.handle, attribs, effective);
        } catch (SWTException e) {
            SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH, e);
        }
        final long finalContext = context;
        dummycanvas.dispose();
        Listener listener = new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.Dispose:
                    deleteContext(finalContext);
                    break;
                }
            }
        };
        canvas.addListener(SWT.Dispose, listener);
        return context;
    }

    private long create(long windowHandle, long dummyWindowHandle, GLData attribs, GLData effective) throws SWTException {
        // Validate context attributes
        validateAttributes(attribs);

        // Find this exact pixel format, though for now without multisampling. This comes later!
        PIXELFORMATDESCRIPTOR pfd = PIXELFORMATDESCRIPTOR.create();
        pfd.nSize((short) PIXELFORMATDESCRIPTOR.SIZEOF);
        pfd.nVersion((short) 1); // this should always be 1
        pfd.dwLayerMask(GDI32.PFD_MAIN_PLANE);
        pfd.iPixelType((byte) GDI32.PFD_TYPE_RGBA);
        int flags = GDI32.PFD_DRAW_TO_WINDOW | GDI32.PFD_SUPPORT_OPENGL;
        if (attribs.doubleBuffer)
            flags |= GDI32.PFD_DOUBLEBUFFER;
        if (attribs.stereo)
            flags |= GDI32.PFD_STEREO;
        pfd.dwFlags(flags);
        pfd.cRedBits((byte) attribs.redSize);
        pfd.cGreenBits((byte) attribs.greenSize);
        pfd.cBlueBits((byte) attribs.blueSize);
        pfd.cAlphaBits((byte) attribs.alphaSize);
        pfd.cDepthBits((byte) attribs.depthSize);
        pfd.cStencilBits((byte) attribs.stencilSize);
        pfd.cAccumRedBits((byte) attribs.accumRedSize);
        pfd.cAccumGreenBits((byte) attribs.accumGreenSize);
        pfd.cAccumBlueBits((byte) attribs.accumBlueSize);
        pfd.cAccumAlphaBits((byte) attribs.accumAlphaSize);
        pfd.cAccumBits((byte) (attribs.accumRedSize + attribs.accumGreenSize + attribs.accumBlueSize + attribs.accumAlphaSize));
        long hDCdummy = User32.GetDC(dummyWindowHandle);
        int pixelFormat = GDI32.ChoosePixelFormat(hDCdummy, pfd);
        if (pixelFormat == 0 || GDI32.SetPixelFormat(hDCdummy, pixelFormat, pfd) == 0) {
            // Pixel format unsupported
            User32.ReleaseDC(dummyWindowHandle, hDCdummy);
            throw new SWTException("Unsupported pixel format");
        }

        /*
         * Next, create a dummy context using Opengl32.lib's wglCreateContext. This should ALWAYS work, but won't give us a "new"/"core" context if we requested
         * that and also does not support multisampling. But we use this "dummy" context then to request the required WGL function pointers to create a new
         * OpenGL >= 3.0 context and with optional multisampling.
         */
        long dummyContext = JNI.callPP(wgl.CreateContext, hDCdummy);
        if (dummyContext == 0L) {
            User32.ReleaseDC(dummyWindowHandle, hDCdummy);
            throw new SWTException("Failed to create OpenGL context");
        }

        final APIBuffer buffer = APIUtil.apiBuffer();

        // Save current context to restore it later
        final long currentContext = JNI.callP(wgl.GetCurrentContext);
        final long currentDc = JNI.callP(wgl.GetCurrentDC);

        // Make the new dummy context current
        int success = JNI.callPPI(wgl.MakeCurrent, hDCdummy, dummyContext);
        if (success == 0) {
            User32.ReleaseDC(dummyWindowHandle, hDCdummy);
            JNI.callPI(wgl.DeleteContext, dummyContext);
            throw new SWTException("Failed to make OpenGL context current");
        }

        // Query supported WGL extensions
        String wglExtensions = null;
        int procEncoded = buffer.stringParamASCII("wglGetExtensionsStringARB", true);
        long adr = buffer.address(procEncoded);
        long wglGetExtensionsStringARBAddr = JNI.callPP(wgl.GetProcAddress, adr);
        if (wglGetExtensionsStringARBAddr != 0L) {
            long str = JNI.callPP(wglGetExtensionsStringARBAddr, hDCdummy);
            if (str != 0L) {
                wglExtensions = MemoryUtil.memDecodeASCII(str);
            } else {
                wglExtensions = "";
            }
        } else {
            // Try the EXT extension
            procEncoded = buffer.stringParamASCII("wglGetExtensionsStringEXT", true);
            adr = buffer.address(procEncoded);
            long wglGetExtensionsStringEXTAddr = JNI.callPP(wgl.GetProcAddress, adr);
            if (wglGetExtensionsStringEXTAddr != 0L) {
                long str = JNI.callP(wglGetExtensionsStringEXTAddr);
                if (str != 0L) {
                    wglExtensions = MemoryUtil.memDecodeASCII(str);
                } else {
                    wglExtensions = "";
                }
            } else {
                wglExtensions = "";
            }
        }
        String[] splitted = wglExtensions.split(" ");
        Set<String> wglExtensionsList = new HashSet<String>(splitted.length);
        for (String str : splitted) {
            wglExtensionsList.add(str);
        }
        success = User32.ReleaseDC(dummyWindowHandle, hDCdummy);
        if (success == 0) {
            JNI.callPI(wgl.DeleteContext, dummyContext);
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            throw new SWTException("Could not release dummy DC");
        }

        // For some constellations of context attributes, we can stop right here.
        if (!atLeast30(attribs.majorVersion, attribs.minorVersion) && attribs.samples == 0 && !attribs.sRGB && !attribs.pixelFormatFloat
                && attribs.contextReleaseBehavior == null && !attribs.robustness && attribs.api != API.GLES) {
            /* Finally, create the real context on the real window */
            long hDC = User32.GetDC(windowHandle);
            GDI32.SetPixelFormat(hDC, pixelFormat, pfd);
            success = JNI.callPI(wgl.DeleteContext, dummyContext);
            if (success == 0) {
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("Could not delete dummy GL context");
            }
            long context = JNI.callPP(wgl.CreateContext, hDC);

            if (attribs.swapInterval != null) {
                boolean has_WGL_EXT_swap_control = wglExtensionsList.contains("WGL_EXT_swap_control");
                if (!has_WGL_EXT_swap_control) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("Swap interval requested but WGL_EXT_swap_control is unavailable");
                }
                if (attribs.swapInterval < 0) {
                    // Only allowed if WGL_EXT_swap_control_tear is available
                    boolean has_WGL_EXT_swap_control_tear = wglExtensionsList.contains("WGL_EXT_swap_control_tear");
                    if (!has_WGL_EXT_swap_control_tear) {
                        User32.ReleaseDC(windowHandle, hDC);
                        JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                        throw new SWTException("Negative swap interval requested but WGL_EXT_swap_control_tear is unavailable");
                    }
                }
                // Make context current to set the swap interval
                success = JNI.callPPI(wgl.MakeCurrent, hDC, context);
                if (success == 0) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("Could not make GL context current");
                }
                procEncoded = buffer.stringParamASCII("wglSwapIntervalEXT", true);
                adr = buffer.address(procEncoded);
                long wglSwapIntervalEXTAddr = JNI.callPP(wgl.GetProcAddress, adr);
                if (wglSwapIntervalEXTAddr != 0L) {
                    JNI.callII(wglSwapIntervalEXTAddr, attribs.swapInterval);
                }
            }

            if (attribs.swapGroupNV > 0 || attribs.swapBarrierNV > 0) {
                // Only allowed if WGL_NV_swap_group is available
                boolean has_WGL_NV_swap_group = wglExtensionsList.contains("WGL_NV_swap_group");
                if (!has_WGL_NV_swap_group) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("Swap group or barrier requested but WGL_NV_swap_group is unavailable");
                }
                // Make context current to join swap group and/or barrier
                success = JNI.callPPI(wgl.MakeCurrent, hDC, context);
                try {
                    wglNvSwapGroupAndBarrier(attribs, buffer, buffer.address(), hDC);
                } catch (SWTException e) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw e;
                }
            }

            success = User32.ReleaseDC(windowHandle, hDC);
            if (success == 0) {
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                JNI.callPI(wgl.DeleteContext, context);
                throw new SWTException("Could not release DC");
            }

            /* Check if we want to share context */
            if (attribs.shareContext != null) {
                success = JNI.callPPI(wgl.ShareLists, context, attribs.shareContext.context);
                if (success == 0) {
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    JNI.callPI(wgl.DeleteContext, context);
                    throw new SWTException("Failed while configuring context sharing.");
                }
            }

            // Describe pixel format
            int pixFmtIndex = GDI32.DescribePixelFormat(hDC, pixelFormat, pfd);
            if (pixFmtIndex == 0) {
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                JNI.callPI(wgl.DeleteContext, context);
                throw new SWTException("Failed to describe pixel format.");
            }
            effective.redSize = pfd.cRedBits();
            effective.greenSize = pfd.cGreenBits();
            effective.blueSize = pfd.cBlueBits();
            effective.alphaSize = pfd.cAlphaBits();
            effective.depthSize = pfd.cDepthBits();
            effective.stencilSize = pfd.cStencilBits();
            int pixelFormatFlags = pfd.dwFlags();
            effective.doubleBuffer = (pixelFormatFlags & GDI32.PFD_DOUBLEBUFFER) != 0;
            effective.stereo = (pixelFormatFlags & GDI32.PFD_STEREO) != 0;
            effective.accumRedSize = pfd.cAccumRedBits();
            effective.accumGreenSize = pfd.cAccumGreenBits();
            effective.accumBlueSize = pfd.cAccumBlueBits();
            effective.accumAlphaSize = pfd.cAccumAlphaBits();

            // Restore old context
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            return context;
        }

        // Check for WGL_ARB_create_context support
        if (!wglExtensionsList.contains("WGL_ARB_create_context")) {
            JNI.callPI(wgl.DeleteContext, dummyContext);
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            throw new SWTException("Extended context attributes requested but WGL_ARB_create_context is unavailable");
        }

        // Obtain wglCreateContextAttribsARB function pointer
        procEncoded = buffer.stringParamASCII("wglCreateContextAttribsARB", true);
        adr = buffer.address(procEncoded);
        long wglCreateContextAttribsARBAddr = JNI.callPP(wgl.GetProcAddress, adr);
        if (wglCreateContextAttribsARBAddr == 0L) {
            JNI.callPI(wgl.DeleteContext, dummyContext);
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            throw new SWTException("WGL_ARB_create_context available but wglCreateContextAttribsARB is NULL");
        }

        IntBuffer attribList = BufferUtils.createIntBuffer(64);
        long attribListAddr = MemoryUtil.memAddress(attribList);
        long hDC = User32.GetDC(windowHandle);

        // Obtain wglChoosePixelFormatARB if multisampling or sRGB or floating point pixel format is requested
        if (attribs.samples > 0 || attribs.sRGB || attribs.pixelFormatFloat) {
            procEncoded = buffer.stringParamASCII("wglChoosePixelFormatARB", true);
            adr = buffer.address(procEncoded);
            long wglChoosePixelFormatAddr = JNI.callPP(wgl.GetProcAddress, adr);
            if (wglChoosePixelFormatAddr == 0L) {
                // Try EXT function (the WGL constants are the same in both extensions)
                procEncoded = buffer.stringParamASCII("wglChoosePixelFormatEXT", true);
                adr = buffer.address(procEncoded);
                wglChoosePixelFormatAddr = JNI.callPP(wgl.GetProcAddress, adr);
                if (wglChoosePixelFormatAddr == 0L) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("No support for wglChoosePixelFormatARB/EXT. Cannot query supported pixel formats.");
                }
            }
            if (attribs.samples > 0) {
                // Check for ARB or EXT extension (their WGL constants have the same value)
                boolean has_WGL_ARB_multisample = wglExtensionsList.contains("WGL_ARB_multisample");
                boolean has_WGL_EXT_multisample = wglExtensionsList.contains("WGL_EXT_multisample");
                if (!has_WGL_ARB_multisample && !has_WGL_EXT_multisample) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("Multisampling requested but neither WGL_ARB_multisample nor WGL_EXT_multisample available");
                }
                if (attribs.colorSamplesNV > 0) {
                    boolean has_WGL_NV_multisample_coverage = wglExtensionsList.contains("WGL_NV_multisample_coverage");
                    if (!has_WGL_NV_multisample_coverage) {
                        User32.ReleaseDC(windowHandle, hDC);
                        JNI.callPI(wgl.DeleteContext, dummyContext);
                        JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                        throw new SWTException("Color samples requested but WGL_NV_multisample_coverage is unavailable");
                    }
                }
            }
            if (attribs.sRGB) {
                // Check for WGL_EXT_framebuffer_sRGB
                boolean has_WGL_EXT_framebuffer_sRGB = wglExtensionsList.contains("WGL_EXT_framebuffer_sRGB");
                if (!has_WGL_EXT_framebuffer_sRGB) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("sRGB color space requested but WGL_EXT_framebuffer_sRGB is unavailable");
                }
            }
            if (attribs.pixelFormatFloat) {
                // Check for WGL_ARB_pixel_format_float
                boolean has_WGL_ARB_pixel_format_float = wglExtensionsList.contains("WGL_ARB_pixel_format_float");
                if (!has_WGL_ARB_pixel_format_float) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("Floating-point format requested but WGL_ARB_pixel_format_float is unavailable");
                }
            }
            // Query matching pixel formats
            encodePixelFormatAttribs(attribList, attribs);
            success = JNI.callPPPIPPI(wglChoosePixelFormatAddr, hDC, attribListAddr, 0L, 1, buffer.address() + 4, buffer.address());
            int numFormats = buffer.buffer().getInt(0);
            if (success == 0 || numFormats == 0) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("No supported pixel format found.");
            }
            pixelFormat = buffer.buffer().getInt(4);
            // Describe pixel format for the PIXELFORMATDESCRIPTOR to match the chosen format
            int pixFmtIndex = GDI32.DescribePixelFormat(hDC, pixelFormat, pfd);
            if (pixFmtIndex == 0) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("Failed to validate supported pixel format.");
            }
            // Obtain extended pixel format attributes
            procEncoded = buffer.stringParamASCII("wglGetPixelFormatAttribivARB", true);
            adr = buffer.address(procEncoded);
            long wglGetPixelFormatAttribivAddr = JNI.callPP(wgl.GetProcAddress, adr);
            if (wglGetPixelFormatAttribivAddr == 0L) {
                // Try EXT function (function signature is the same)
                procEncoded = buffer.stringParamASCII("wglGetPixelFormatAttribivEXT", true);
                adr = buffer.address(procEncoded);
                wglGetPixelFormatAttribivAddr = JNI.callPP(wgl.GetProcAddress, adr);
                if (wglGetPixelFormatAttribivAddr == 0L) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException("No support for wglGetPixelFormatAttribivARB/EXT. Cannot get effective pixel format attributes.");
                }
            }
            attribList.rewind();
            attribList.put(WGLARBPixelFormat.WGL_DOUBLE_BUFFER_ARB);
            attribList.put(WGLARBPixelFormat.WGL_STEREO_ARB);
            attribList.put(WGLARBPixelFormat.WGL_PIXEL_TYPE_ARB);
            attribList.put(WGLARBPixelFormat.WGL_RED_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_GREEN_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_BLUE_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_ALPHA_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_ACCUM_RED_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_ACCUM_GREEN_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_ACCUM_BLUE_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_ACCUM_ALPHA_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_DEPTH_BITS_ARB);
            attribList.put(WGLARBPixelFormat.WGL_STENCIL_BITS_ARB);
            IntBuffer attribValues = BufferUtils.createIntBuffer(attribList.position());
            long attribValuesAddr = MemoryUtil.memAddress(attribValues);
            success = JNI.callPIIIPPI(wglGetPixelFormatAttribivAddr, hDC, pixelFormat, GDI32.PFD_MAIN_PLANE, attribList.position(), attribListAddr,
                    attribValuesAddr);
            if (success == 0) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("Failed to get pixel format attributes.");
            }
            effective.doubleBuffer = attribValues.get(0) == 1;
            effective.stereo = attribValues.get(1) == 1;
            int pixelType = attribValues.get(2);
            effective.pixelFormatFloat = pixelType == WGLARBPixelFormatFloat.WGL_TYPE_RGBA_FLOAT_ARB;
            effective.redSize = attribValues.get(3);
            effective.greenSize = attribValues.get(4);
            effective.blueSize = attribValues.get(5);
            effective.alphaSize = attribValues.get(6);
            effective.accumRedSize = attribValues.get(7);
            effective.accumGreenSize = attribValues.get(8);
            effective.accumBlueSize = attribValues.get(9);
            effective.accumAlphaSize = attribValues.get(10);
            effective.depthSize = attribValues.get(11);
            effective.stencilSize = attribValues.get(12);
        }

        // Compose the attributes list
        attribList.rewind();
        if (attribs.api == API.GL && atLeast30(attribs.majorVersion, attribs.minorVersion) ||
            attribs.api == API.GLES && attribs.majorVersion > 0) {
            attribList.put(WGLARBCreateContext.WGL_CONTEXT_MAJOR_VERSION_ARB).put(attribs.majorVersion);
            attribList.put(WGLARBCreateContext.WGL_CONTEXT_MINOR_VERSION_ARB).put(attribs.minorVersion);
        }
        int profile = 0;
        if (attribs.api == API.GL) {
            if (attribs.profile == Profile.COMPATIBILITY) {
                profile = WGLARBCreateContextProfile.WGL_CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB;
            } else if (attribs.profile == Profile.CORE) {
                profile = WGLARBCreateContextProfile.WGL_CONTEXT_CORE_PROFILE_BIT_ARB;
            }
        } else if (attribs.api == API.GLES) {
            boolean has_WGL_EXT_create_context_es2_profile = wglExtensionsList.contains("WGL_EXT_create_context_es2_profile");
            if (!has_WGL_EXT_create_context_es2_profile) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("OpenGL ES API requested but WGL_EXT_create_context_es2_profile is unavailable");
            }
            profile = WGLEXTCreateContextES2Profile.WGL_CONTEXT_ES2_PROFILE_BIT_EXT;
        }
        if (profile > 0) {
            boolean has_WGL_ARB_create_context_profile = wglExtensionsList.contains("WGL_ARB_create_context_profile");
            if (!has_WGL_ARB_create_context_profile) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("OpenGL profile requested but WGL_ARB_create_context_profile is unavailable");
            }
            attribList.put(WGLARBCreateContextProfile.WGL_CONTEXT_PROFILE_MASK_ARB).put(profile);
        }
        int contextFlags = 0;
        if (attribs.debug) {
            contextFlags |= WGLARBCreateContext.WGL_CONTEXT_DEBUG_BIT_ARB;
        }
        if (attribs.forwardCompatible) {
            contextFlags |= WGLARBCreateContext.WGL_CONTEXT_FORWARD_COMPATIBLE_BIT_ARB;
        }
        if (attribs.robustness) {
            // Check for WGL_ARB_create_context_robustness
            boolean has_WGL_ARB_create_context_robustness = wglExtensions.contains("WGL_ARB_create_context_robustness");
            if (!has_WGL_ARB_create_context_robustness) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("Context with robust buffer access requested but WGL_ARB_create_context_robustness is unavailable");
            }
            contextFlags |= WGLARBCreateContextRobustness.WGL_CONTEXT_ROBUST_ACCESS_BIT_ARB;
            if (attribs.loseContextOnReset) {
                attribList.put(WGLARBCreateContextRobustness.WGL_CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB).put(
                        WGLARBCreateContextRobustness.WGL_LOSE_CONTEXT_ON_RESET_ARB);
                // Note: WGL_NO_RESET_NOTIFICATION_ARB is default behaviour and need not be specified.
            }
            if (attribs.contextResetIsolation) {
                // Check for WGL_ARB_robustness_application_isolation or WGL_ARB_robustness_share_group_isolation
                boolean has_WGL_ARB_robustness_application_isolation = wglExtensions.contains("WGL_ARB_robustness_application_isolation");
                boolean has_WGL_ARB_robustness_share_group_isolation = wglExtensions.contains("WGL_ARB_robustness_share_group_isolation");
                if (!has_WGL_ARB_robustness_application_isolation && !has_WGL_ARB_robustness_share_group_isolation) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPI(wgl.DeleteContext, dummyContext);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    throw new SWTException(
                            "Robustness isolation requested but neither WGL_ARB_robustness_application_isolation nor WGL_ARB_robustness_share_group_isolation available");
                }
                contextFlags |= WGLARBRobustnessApplicationIsolation.WGL_CONTEXT_RESET_ISOLATION_BIT_ARB;
            }
        }
        if (contextFlags > 0)
            attribList.put(WGLARBCreateContext.WGL_CONTEXT_FLAGS_ARB).put(contextFlags);
        if (attribs.contextReleaseBehavior != null) {
            boolean has_WGL_ARB_context_flush_control = wglExtensionsList.contains("WGL_ARB_context_flush_control");
            if (!has_WGL_ARB_context_flush_control) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPI(wgl.DeleteContext, dummyContext);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                throw new SWTException("Context release behavior requested but WGL_ARB_context_flush_control is unavailable");
            }
            if (attribs.contextReleaseBehavior == ReleaseBehavior.NONE)
                attribList.put(WGLARBContextFlushControl.WGL_CONTEXT_RELEASE_BEHAVIOR_ARB).put(WGLARBContextFlushControl.WGL_CONTEXT_RELEASE_BEHAVIOR_NONE_ARB);
            else if (attribs.contextReleaseBehavior == ReleaseBehavior.FLUSH)
                attribList.put(WGLARBContextFlushControl.WGL_CONTEXT_RELEASE_BEHAVIOR_ARB)
                        .put(WGLARBContextFlushControl.WGL_CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB);
        }
        attribList.put(0).put(0);
        // Set pixelformat
        int succ = GDI32.SetPixelFormat(hDC, pixelFormat, pfd);
        if (succ == 0) {
            User32.ReleaseDC(windowHandle, hDC);
            JNI.callPI(wgl.DeleteContext, dummyContext);
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            throw new SWTException("Failed to set pixel format.");
        }
        // And create new context with it
        long newCtx = JNI.callPPPP(wglCreateContextAttribsARBAddr, hDC, attribs.shareContext != null ? attribs.shareContext.context : 0L, attribListAddr);
        JNI.callPI(wgl.DeleteContext, dummyContext);
        if (newCtx == 0L) {
            User32.ReleaseDC(windowHandle, hDC);
            JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
            throw new SWTException("Failed to create OpenGL context.");
        }
        // Make context current for next operations
        JNI.callPPI(wgl.MakeCurrent, hDC, newCtx);
        if (attribs.swapInterval != null) {
            boolean has_WGL_EXT_swap_control = wglExtensionsList.contains("WGL_EXT_swap_control");
            if (!has_WGL_EXT_swap_control) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                JNI.callPI(wgl.DeleteContext, newCtx);
                throw new SWTException("Swap interval requested but WGL_EXT_swap_control is unavailable");
            }
            if (attribs.swapInterval < 0) {
                // Only allowed if WGL_EXT_swap_control_tear is available
                boolean has_WGL_EXT_swap_control_tear = wglExtensionsList.contains("WGL_EXT_swap_control_tear");
                if (!has_WGL_EXT_swap_control_tear) {
                    User32.ReleaseDC(windowHandle, hDC);
                    JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                    JNI.callPI(wgl.DeleteContext, newCtx);
                    throw new SWTException("Negative swap interval requested but WGL_EXT_swap_control_tear is unavailable");
                }
            }
            procEncoded = buffer.stringParamASCII("wglSwapIntervalEXT", true);
            adr = buffer.address(procEncoded);
            long wglSwapIntervalEXTAddr = JNI.callPP(wgl.GetProcAddress, adr);
            if (wglSwapIntervalEXTAddr != 0L) {
                JNI.callII(wglSwapIntervalEXTAddr, attribs.swapInterval);
            }
        }
        if (attribs.swapGroupNV > 0 || attribs.swapBarrierNV > 0) {
            // Only allowed if WGL_NV_swap_group is available
            boolean has_WGL_NV_swap_group = wglExtensionsList.contains("WGL_NV_swap_group");
            if (!has_WGL_NV_swap_group) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                JNI.callPI(wgl.DeleteContext, newCtx);
                throw new SWTException("Swap group or barrier requested but WGL_NV_swap_group is unavailable");
            }
            try {
                wglNvSwapGroupAndBarrier(attribs, buffer, buffer.address(), hDC);
            } catch (SWTException e) {
                User32.ReleaseDC(windowHandle, hDC);
                JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
                JNI.callPI(wgl.DeleteContext, newCtx);
                throw e;
            }
        }
        User32.ReleaseDC(windowHandle, hDC);
        long getInteger = gl.getFunctionAddress("glGetIntegerv");
        long getString = gl.getFunctionAddress("glGetString");
        effective.api = attribs.api;
        if (atLeast30(attribs.majorVersion, attribs.minorVersion)) {
            JNI.callIPV(getInteger, GL30.GL_MAJOR_VERSION, buffer.address());
            effective.majorVersion = buffer.intValue(0);
            JNI.callIPV(getInteger, GL30.GL_MINOR_VERSION, buffer.address());
            effective.minorVersion = buffer.intValue(0);
            JNI.callIPV(getInteger, GL30.GL_CONTEXT_FLAGS, buffer.address());
            int effectiveContextFlags = buffer.intValue(0);
            effective.debug = (effectiveContextFlags & GL43.GL_CONTEXT_FLAG_DEBUG_BIT) != 0;
            effective.forwardCompatible = (effectiveContextFlags & GL30.GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT) != 0;
            effective.robustness = (effectiveContextFlags & ARBRobustness.GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB) != 0;
        } else if (attribs.api == API.GL) {
            APIVersion version = APIUtil.apiParseVersion(MemoryUtil.memDecodeUTF8(Checks.checkPointer(JNI.callIP(getString, GL11.GL_VERSION))));
            effective.majorVersion = version.major;
            effective.minorVersion = version.minor;
        } else if (attribs.api == API.GLES) {
            APIVersion version = APIUtil.apiParseVersion(MemoryUtil.memDecodeUTF8(Checks.checkPointer(JNI.callIP(getString, GL11.GL_VERSION))), "OpenGL ES");
            effective.majorVersion = version.major;
            effective.minorVersion = version.minor;
        }
        if (attribs.api == API.GL && atLeast32(effective.majorVersion, effective.minorVersion)) {
            JNI.callIPV(getInteger, GL32.GL_CONTEXT_PROFILE_MASK, buffer.address());
            int effectiveProfileMask = buffer.intValue(0);
            boolean core = (effectiveProfileMask & GL32.GL_CONTEXT_CORE_PROFILE_BIT) != 0;
            boolean comp = (effectiveProfileMask & GL32.GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) != 0;
            if (comp) {
                effective.profile = Profile.COMPATIBILITY;
            } else if (core) {
                effective.profile = Profile.CORE;
            } else {
                effective.profile = null;
            }
        }
        if (attribs.samples >= 1) {
            JNI.callIPV(getInteger, ARBMultisample.GL_SAMPLES_ARB, buffer.address());
            effective.samples = buffer.intValue(0);
            JNI.callIPV(getInteger, ARBMultisample.GL_SAMPLE_BUFFERS_ARB, buffer.address());
            effective.sampleBuffers = buffer.intValue(0);
            boolean has_WGL_NV_multisample_coverage = wglExtensionsList.contains("WGL_NV_multisample_coverage");
            if (has_WGL_NV_multisample_coverage) {
                JNI.callIPV(getInteger, NVMultisampleCoverage.GL_COLOR_SAMPLES_NV, buffer.address());
                effective.colorSamplesNV = buffer.intValue(0);
            }
        }
        // Restore old context
        JNI.callPPI(wgl.MakeCurrent, currentDc, currentContext);
        return newCtx;
    }

    private void wglNvSwapGroupAndBarrier(GLData attribs, APIBuffer buffer, long bufferAddr, long hDC) throws SWTException {
        int success;
        int procEncoded;
        long adr;
        procEncoded = buffer.stringParamASCII("wglQueryMaxSwapGroupsNV", true);
        adr = buffer.address(procEncoded);
        long wglQueryMaxSwapGroupsNVAddr = JNI.callPP(wgl.GetProcAddress, adr);
        success = JNI.callPPPI(wglQueryMaxSwapGroupsNVAddr, hDC, bufferAddr, bufferAddr + 4);
        int maxGroups = buffer.buffer().getInt(0);
        if (maxGroups < attribs.swapGroupNV) {
            throw new SWTException("Swap group exceeds maximum group index");
        }
        int maxBarriers = buffer.buffer().getInt(4);
        if (maxBarriers < attribs.swapBarrierNV) {
            throw new SWTException("Swap barrier exceeds maximum barrier index");
        }
        if (attribs.swapGroupNV > 0) {
            procEncoded = buffer.stringParamASCII("wglJoinSwapGroupNV", true);
            adr = buffer.address(procEncoded);
            long wglJoinSwapGroupNVAddr = JNI.callPP(wgl.GetProcAddress, adr);
            if (wglJoinSwapGroupNVAddr == 0L) {
                throw new SWTException("WGL_NV_swap_group available but wglJoinSwapGroupNV is NULL");
            }
            success = JNI.callPII(wglJoinSwapGroupNVAddr, hDC, attribs.swapGroupNV);
            if (success == 0) {
                throw new SWTException("Failed to join swap group");
            }
            if (attribs.swapBarrierNV > 0) {
                procEncoded = buffer.stringParamASCII("wglBindSwapBarrierNV", true);
                adr = buffer.address(procEncoded);
                long wglBindSwapBarrierNVAddr = JNI.callPP(wgl.GetProcAddress, adr);
                if (wglBindSwapBarrierNVAddr == 0L) {
                    throw new SWTException("WGL_NV_swap_group available but wglBindSwapBarrierNV is NULL");
                }
                success = JNI.callIII(wglBindSwapBarrierNVAddr, attribs.swapGroupNV, attribs.swapBarrierNV);
                if (success == 0) {
                    throw new SWTException("Failed to bind swap barrier. Probably no G-Sync card installed.");
                }
            }
        }
    }

    public boolean isCurrent(long context) {
        long ret = JNI.callP(wgl.GetCurrentContext);
        return ret == context;
    }

    public boolean makeCurrent(GLCanvas canvas, long context) {
        long hDC = User32.GetDC(canvas.handle);
        int ret = JNI.callPPI(wgl.MakeCurrent, hDC, context);
        User32.ReleaseDC(canvas.handle, hDC);
        return ret == 1;
    }

    public boolean deleteContext(long context) {
        int ret = JNI.callPI(wgl.DeleteContext, context);
        return ret == 1;
    }

    public boolean swapBuffers(GLCanvas canvas) {
        long hDC = User32.GetDC(canvas.handle);
        int ret = GDI32.SwapBuffers(hDC);
        User32.ReleaseDC(canvas.handle, hDC);
        return ret == 1;
    }

    public boolean delayBeforeSwapNV(GLCanvas canvas, float seconds) {
        if (!wglDelayBeforeSwapNVAddr_set) {
            APIBuffer buffer = APIUtil.apiBuffer();
            int procEncoded = buffer.stringParamASCII("wglDelayBeforeSwapNV", true);
            long adr = buffer.address(procEncoded);
            wglDelayBeforeSwapNVAddr = JNI.callPP(wgl.GetProcAddress, adr);
            wglDelayBeforeSwapNVAddr_set = true;
        }
        if (wglDelayBeforeSwapNVAddr == 0L) {
            throw new UnsupportedOperationException("wglDelayBeforeSwapNV is unavailable");
        }
        long hDC = User32.GetDC(canvas.handle);
        int ret = JNI.callPFI(wglDelayBeforeSwapNVAddr, hDC, seconds);
        User32.ReleaseDC(canvas.handle, hDC);
        return ret == 1;
    }

}