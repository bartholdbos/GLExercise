package cloud.bos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;

    public ShaderProgram() {
        programId = glCreateProgram();

        if (programId == 0) {
            throw new RuntimeException("Could not create ShaderProgram");
        }
    }

    public void loadShader(File shaderFile, int shaderType) throws IOException {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw new RuntimeException("Could not create Shader");
        } else if (shaderId == GL_INVALID_ENUM) {
            throw new RuntimeException("Invalid ShaderType: " + shaderType);
        }

        glShaderSource(shaderId, readFileToString(shaderFile));
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);
    }

    private String readFileToString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    public void link() {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking ShaderProgram: " + glGetProgramInfoLog(programId));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public static void unbind() {
        glUseProgram(0);
    }

    /**
     * TEMP
     * @return int
     */
    public int getProgramId(){
        return programId;
    }
}
