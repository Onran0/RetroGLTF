package io.github.onran0.retrogltf;

import io.github.onran0.retrogltf.loader.GLTFLoadException;
import io.github.onran0.retrogltf.loader.GLTFLoader;
import io.github.onran0.retrogltf.loader.LoaderWarmup;
import io.github.onran0.retrogltf.loader.Profiler;
import io.github.onran0.retrogltf.render.SceneRenderer;
import io.github.onran0.retrogltf.util.GLCleaner;
import org.joml.Vector3f;
import org.json.JSONObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class RenderTest {

    private static void renderCube() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(1, 0, 0);
        GL11.glVertex3f(-1, 1, 1); GL11.glVertex3f(-1, -1, 1); GL11.glVertex3f(1, -1, 1); GL11.glVertex3f(1, 1, 1);
        GL11.glColor3f(0, 1, 0);
        GL11.glVertex3f(-1, 1, -1); GL11.glVertex3f(1, 1, -1); GL11.glVertex3f(1, -1, -1); GL11.glVertex3f(-1, -1, -1);
        GL11.glColor3f(0, 0, 1);
        GL11.glVertex3f(-1, 1, -1); GL11.glVertex3f(-1, 1, 1); GL11.glVertex3f(1, 1, 1); GL11.glVertex3f(1, 1, -1);
        GL11.glColor3f(1, 1, 0);
        GL11.glVertex3f(-1, -1, -1); GL11.glVertex3f(1, -1, -1); GL11.glVertex3f(1, -1, 1); GL11.glVertex3f(-1, -1, 1);
        GL11.glColor3f(0, 1, 1);
        GL11.glVertex3f(1, -1, -1); GL11.glVertex3f(1, 1, -1); GL11.glVertex3f(1, 1, 1); GL11.glVertex3f(1, -1, 1);
        GL11.glColor3f(1, 0, 1);
        GL11.glVertex3f(-1, -1, -1); GL11.glVertex3f(-1, -1, 1); GL11.glVertex3f(-1, 1, 1); GL11.glVertex3f(-1, 1, -1);
        GL11.glEnd();
    }

    public static void main(String[] args) throws LWJGLException, IOException, GLTFLoadException {
        boolean input = true;

        Display.setDisplayMode(new DisplayMode(800, 600));

        Display.setTitle("Test");

        Display.create();

        if(input) {
            Mouse.setGrabbed(true);
        }

        System.out.println("Loading glTF...");

        Scene scene = null;

        JSONObject json = new JSONObject(
                new String(
                        Files.readAllBytes(
                                Paths.get("test/old_rusty_car/scene.gltf")
                        ),
                        StandardCharsets.UTF_8
                )
        );

        Path root = Paths.get("test/old_rusty_car");

        LoaderWarmup.initializeClasses();

        scene = new GLTFLoader(
                json, root
        ).load();

        GLCleaner.freeScene(scene);

        Profiler.clear();

        scene = new GLTFLoader(
                json, root
        ).load();

        Profiler.printMillis();

        Node headNode = scene.findNodeByName("mixamorig:Head_06");

        Node leftArmNode = scene.findNodeByName("mixamorig:LeftArm_09");
        Node rightArmNode = scene.findNodeByName("mixamorig:RightArm_00");

        Vector3f headNodeEuler = new Vector3f();
        Vector3f rightArmNodeEuler = new Vector3f();
        Vector3f leftArmNodeEuler = new Vector3f();

//        headNode.getEulerAngles(headNodeEuler);
//        rightArmNode.getEulerAngles(rightArmNodeEuler);
//        leftArmNode.getEulerAngles(leftArmNodeEuler);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        float fov = 60.0f, aspect = 800f/600f, near = 0.1f, far = 100.0f;
        float fh = (float) Math.tan(Math.toRadians(fov / 2)) * near;
        GL11.glFrustum(-fh * aspect, fh * aspect, -fh, fh, near, far);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        SceneRenderer sceneRenderer = new SceneRenderer();

        float x = 5, y = 5, z = 5, pitch = 38, yaw = 315;

        float time = 0.0f;

        while (!Display.isCloseRequested()) {
            if(input) {
                yaw += Mouse.getDX() * 0.15f;
                pitch = Math.max(-89f, Math.min(89f, pitch - Mouse.getDY() * 0.15f));

                float speed = 0.15f;
                float rad = (float) Math.toRadians(yaw);
                if (Keyboard.isKeyDown(Keyboard.KEY_W)) { x += Math.sin(rad) * speed; z -= Math.cos(rad) * speed; }
                if (Keyboard.isKeyDown(Keyboard.KEY_S)) { x -= Math.sin(rad) * speed; z += Math.cos(rad) * speed; }
                if (Keyboard.isKeyDown(Keyboard.KEY_A)) { x -= Math.cos(rad) * speed; z -= Math.sin(rad) * speed; }
                if (Keyboard.isKeyDown(Keyboard.KEY_D)) { x += Math.cos(rad) * speed; z += Math.sin(rad) * speed; }

                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) { y += speed;  }
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) { y -= speed;  }
            }

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor(0.35f, 0.35f, 0.35f, 1.0f);
            GL11.glLoadIdentity();

            GL11.glRotatef(pitch, 1, 0, 0);
            GL11.glRotatef(yaw, 0, 1, 0);
            GL11.glTranslatef(-x, -y, -z);

            GL11.glScalef(0.01f, 0.01f, 0.01f);

            //renderCube();

            headNodeEuler.set((float) Math.toRadians(Math.sin(time) * 40.0D), 0, 0);
            rightArmNodeEuler.set((float) Math.toRadians(Math.sin(time) * 65.0D), 0, 0);
            leftArmNodeEuler.set((float) Math.toRadians(Math.sin(time) * 65.0D), 0, 0);

//            headNode.setEulerAngles(headNodeEuler);
//            rightArmNode.setEulerAngles(rightArmNodeEuler);
//            leftArmNode.setEulerAngles(leftArmNodeEuler);

            sceneRenderer.render(scene);

            time += 1/60f * 2.5f;

            Display.update();

            Display.sync(60);
        }

        Display.destroy();
    }
}