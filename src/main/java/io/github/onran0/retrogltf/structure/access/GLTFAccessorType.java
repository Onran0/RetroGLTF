package io.github.onran0.retrogltf.structure.access;

public enum GLTFAccessorType {
    SCALAR("SCALAR", 1),
    VEC2("VEC2", 2),
    VEC3("VEC3", 3),
    VEC4("VEC4", 4),
    MAT2("MAT2", 4),
    MAT3("MAT3", 9),
    MAT4("MAT4", 16),
    ;

    private final String id;
    private final int numberOfComps;

    GLTFAccessorType(String id, int numberOfComps) {
        this.id = id;
        this.numberOfComps = numberOfComps;
    }

    public String getId() {
        return id;
    }

    public int getNumberOfComponents() {
        return numberOfComps;
    }

    public static GLTFAccessorType getById(String id) {
        for(GLTFAccessorType type : GLTFAccessorType.values()) {
            if(type.getId().equals(id))
                return type;
        }

        throw new IllegalArgumentException(id);
    }
}