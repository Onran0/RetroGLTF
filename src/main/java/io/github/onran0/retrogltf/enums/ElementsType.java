package io.github.onran0.retrogltf.enums;

public enum ElementsType {
    POINTS(0),
    LINE_STRIPS(1),
    LINE_LOOPS(2),
    LINES(3),
    TRIANGLES(4),
    TRIANGLE_STRIPS(5),
    TRIANGLE_FANS(6);

    private final int id;
    private final int glType;

    ElementsType(int id) {
        this.id = id;
        this.glType = id;
    }

    public int getId() {
        return id;
    }

    public int getGLType() {
        return glType;
    }

    public static ElementsType getById(int id) {
        for(ElementsType type : ElementsType.values()) {
            if(type.getId() == id)
                return type;
        }

        throw new IllegalArgumentException(String.valueOf(id));
    }
}