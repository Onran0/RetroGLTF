package io.github.onran0.retrogltf;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public final class RenderTest {

    public static void main(String[] args) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(800, 600));

        Display.setTitle("Test");

        Display.create();

        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

        while (!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(-0.5f,  0.5f);
            GL11.glVertex2f( 0.5f,  0.5f);
            GL11.glVertex2f( 0.5f, -0.5f);
            GL11.glVertex2f(-0.5f, -0.5f);
            GL11.glEnd();

            Display.update();

            Display.sync(60);
        }

        Display.destroy();
    }
}