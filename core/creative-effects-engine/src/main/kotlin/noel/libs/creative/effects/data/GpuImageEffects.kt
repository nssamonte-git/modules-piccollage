package noel.libs.creative.effects.data

import android.graphics.Bitmap
import android.opengl.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import noel.libs.creative.effects.domain.validateEffect
import noel.libs.creative.model.Effect
import noel.libs.creative.renderer.data.ImageEffects

/** Per-call EGL ownership permits use on any worker thread and deterministic cleanup. */
class GpuImageEffects(private val backendSelected: (String) -> Unit = {}) : ImageEffects {
    override fun apply(source: Bitmap, effects: List<Effect>): Bitmap {
        require(source.width.toLong() * source.height <= 16_000_000 && effects.size <= 32)
        effects.forEach(::validateEffect)
        return try {
            val result = render(source, effects)
            backendSelected("OpenGL ES 2")
            result
        } catch (e: RuntimeException) {
            backendSelected("CPU fallback")
            CpuImageEffects().apply(source, effects)
        }
    }

    private fun render(source: Bitmap, effects: List<Effect>): Bitmap {
        val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        check(display != EGL14.EGL_NO_DISPLAY)
        val versions = IntArray(2)
        check(EGL14.eglInitialize(display, versions, 0, versions, 1))
        var context = EGL14.EGL_NO_CONTEXT
        var surface = EGL14.EGL_NO_SURFACE
        try {
            val configs = arrayOfNulls<android.opengl.EGLConfig>(1)
            val count = IntArray(1)
            check(
                EGL14.eglChooseConfig(
                    display,
                    intArrayOf(
                        EGL14.EGL_RENDERABLE_TYPE,
                        EGL14.EGL_OPENGL_ES2_BIT,
                        EGL14.EGL_SURFACE_TYPE,
                        EGL14.EGL_PBUFFER_BIT,
                        EGL14.EGL_RED_SIZE,
                        8,
                        EGL14.EGL_GREEN_SIZE,
                        8,
                        EGL14.EGL_BLUE_SIZE,
                        8,
                        EGL14.EGL_ALPHA_SIZE,
                        8,
                        EGL14.EGL_NONE,
                    ),
                    0,
                    configs,
                    0,
                    1,
                    count,
                    0,
                ) && count[0] > 0
            )
            context =
                EGL14.eglCreateContext(
                    display,
                    configs[0],
                    EGL14.EGL_NO_CONTEXT,
                    intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE),
                    0,
                )
            surface =
                EGL14.eglCreatePbufferSurface(
                    display,
                    configs[0],
                    intArrayOf(
                        EGL14.EGL_WIDTH,
                        source.width,
                        EGL14.EGL_HEIGHT,
                        source.height,
                        EGL14.EGL_NONE,
                    ),
                    0,
                )
            check(
                context != EGL14.EGL_NO_CONTEXT &&
                    surface != EGL14.EGL_NO_SURFACE &&
                    EGL14.eglMakeCurrent(display, surface, surface, context)
            )
            val vertices =
                ByteBuffer.allocateDirect(16 * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(
                            floatArrayOf(
                                -1f,
                                -1f,
                                0f,
                                0f,
                                1f,
                                -1f,
                                1f,
                                0f,
                                -1f,
                                1f,
                                0f,
                                1f,
                                1f,
                                1f,
                                1f,
                                1f,
                            )
                        )
                        position(0)
                    }
            val vertex =
                shader(
                    GLES20.GL_VERTEX_SHADER,
                    "attribute vec2 position;attribute vec2 uv;varying vec2 tex;void main(){tex=uv;gl_Position=vec4(position,0.,1.);}",
                )
            val fragment = shader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT)
            val program = GLES20.glCreateProgram()
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            val status = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0)
            check(status[0] != 0)
            GLES20.glUseProgram(program)
            GLES20.glViewport(0, 0, source.width, source.height)
            val position = GLES20.glGetAttribLocation(program, "position")
            val uv = GLES20.glGetAttribLocation(program, "uv")
            vertices.position(0)
            GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 16, vertices)
            GLES20.glEnableVertexAttribArray(position)
            vertices.position(2)
            GLES20.glVertexAttribPointer(uv, 2, GLES20.GL_FLOAT, false, 16, vertices)
            GLES20.glEnableVertexAttribArray(uv)
            val textures = IntArray(2)
            GLES20.glGenTextures(2, textures, 0)
            textures.forEach { id ->
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)
                GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST,
                )
                GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_NEAREST,
                )
                GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE,
                )
                GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE,
                )
                GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA,
                    source.width,
                    source.height,
                    0,
                    GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,
                    null,
                )
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, source, 0)
            val framebuffers = IntArray(1)
            GLES20.glGenFramebuffers(1, framebuffers, 0)
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffers[0])
            var index = 0
            GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "image"), 0)
            GLES20.glUniform2f(
                GLES20.glGetUniformLocation(program, "pixel"),
                1f / source.width,
                1f / source.height,
            )
            for (effect in effects.ifEmpty { listOf(Effect(Effect.Kind.BRIGHTNESS, 0f)) }) {
                val out = 1 - index
                GLES20.glFramebufferTexture2D(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D,
                    textures[out],
                    0,
                )
                check(
                    GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) ==
                        GLES20.GL_FRAMEBUFFER_COMPLETE
                )
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[index])
                GLES20.glUniform1i(
                    GLES20.glGetUniformLocation(program, "kind"),
                    effect.kind.ordinal,
                )
                GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "amount"), effect.amount)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
                check(GLES20.glGetError() == GLES20.GL_NO_ERROR)
                index = out
            }
            val bytes = ByteBuffer.allocateDirect(source.width * source.height * 4)
            GLES20.glReadPixels(
                0,
                0,
                source.width,
                source.height,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                bytes,
            )
            val pixels =
                IntArray(source.width * source.height) { i ->
                    val a = bytes.get(i * 4 + 3).toInt() and 255
                    fun channel(offset: Int): Int =
                        if (a == 0) 0
                        else
                            ((bytes.get(i * 4 + offset).toInt() and 255) * 255f / a)
                                .toInt()
                                .coerceIn(0, 255)
                    (a shl 24) or (channel(0) shl 16) or (channel(1) shl 8) or channel(2)
                }
            GLES20.glDeleteFramebuffers(1, framebuffers, 0)
            GLES20.glDeleteTextures(2, textures, 0)
            GLES20.glDeleteProgram(program)
            GLES20.glDeleteShader(vertex)
            GLES20.glDeleteShader(fragment)
            return Bitmap.createBitmap(pixels, source.width, source.height, Bitmap.Config.ARGB_8888)
        } finally {
            EGL14.eglMakeCurrent(
                display,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT,
            )
            if (surface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(display, surface)
            if (context != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(display, context)
            EGL14.eglTerminate(display)
            EGL14.eglReleaseThread()
        }
    }

    private fun shader(type: Int, source: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)
        val status = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
        check(status[0] != 0)
        return shader
    }

    companion object {
        private const val FRAGMENT =
            """precision highp float;
   varying vec2 tex;uniform sampler2D image;uniform int kind;uniform float amount;uniform vec2 pixel;
   void main(){vec4 c=texture2D(image,tex);
    if(kind==4){vec4 total=vec4(0.);float n=0.;float radius=floor(amount+0.5);for(int y=-8;y<=8;y++){for(int x=-8;x<=8;x++){if(abs(float(x))<=radius && abs(float(y))<=radius){total+=texture2D(image,tex+vec2(float(x),float(y))*pixel);n+=1.;}}}gl_FragColor=total/n;return;}
    vec3 rgb=c.a>0.?c.rgb/c.a:vec3(0.);float l=dot(rgb,vec3(.2126,.7152,.0722));
    if(kind==0)rgb+=amount;else if(kind==1)rgb=(rgb-.5)*amount+.5;else if(kind==2)rgb=vec3(l)+(rgb-vec3(l))*amount;else if(kind==3)rgb=mix(rgb,vec3(l),amount);
    gl_FragColor=vec4(clamp(rgb,0.,1.)*c.a,c.a);
   }"""
    }
}
