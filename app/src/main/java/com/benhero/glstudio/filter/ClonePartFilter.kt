package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.R
import com.benhero.glstudio.util.TextResourceReader

/**
 * 部分分身克隆滤镜
 *
 * @author Benhero
 * @date   2019/1/18
 */
class ClonePartFilter(context: Context) : BaseFilter(context, VERTEX_SHADER,
        TextResourceReader.readTextFileFromResource(context, R.raw.filter_test)) {
    companion object {
        const val FRAGMENT_SHADER = """
            precision mediump float;
            varying vec2 v_TexCoord;
            uniform sampler2D u_TextureUnit;
            uniform float isVertical;
            uniform float isHorizontal;
            uniform float cloneCount;
            void main() {
                vec4 source = texture2D(u_TextureUnit, v_TexCoord);
                float coordX = v_TexCoord.x;
                float coordY = v_TexCoord.y;
                if (isVertical == 1.0) {
                    float width = 1.0 / cloneCount;
                    float startX = (1.0 - width) / 2.0;
                    coordX = mod(v_TexCoord.x, width) + startX;
                }
                if (isHorizontal == 1.0) {
                    float height = 1.0 / cloneCount;
                    float startY = (1.0 - height) / 2.0;
                    coordY = mod(v_TexCoord.y, height) + startY;
                }
                gl_FragColor = texture2D(u_TextureUnit, vec2(coordX, coordY));
            }
        """
    }

    var isVerticalLocation: Int = 0
    var isHorizontalLocation: Int = 0
    var isCloneCountLocation: Int = 0
    override fun onCreated() {
        super.onCreated()
        isVerticalLocation = GLES20.glGetUniformLocation(program, "isVertical")
        isHorizontalLocation = GLES20.glGetUniformLocation(program, "isHorizontal")
        isCloneCountLocation = GLES20.glGetUniformLocation(program, "cloneCount")

        GLES20.glUniform1f(isVerticalLocation, 1.0f)
        GLES20.glUniform1f(isHorizontalLocation, 1.0f)
        GLES20.glUniform1f(isCloneCountLocation, 3.0f)
    }
}