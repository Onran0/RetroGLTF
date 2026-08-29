package io.github.onran0.retrogltf.loader.structure.access;

public enum GLTFAccessorType {
    SCALAR("SCALAR", 1),
    VEC2("VEC2", 2),
    VEC3("VEC3", 3),
    VEC4("VEC4", 4),
    MAT2("MAT2", 4, 2),
    MAT3("MAT3", 9, 3),
    MAT4("MAT4", 16, 4);

    private final String id;
    private final int numberOfComps;
    private final boolean isMatrixType;
    private final int matrixDimension;

    GLTFAccessorType(String id, int numberOfComps) {
        this.id = id;
        this.numberOfComps = numberOfComps;
        this.isMatrixType = false;
        this.matrixDimension = 0;
    }

    GLTFAccessorType(String id, int numberOfComps, Integer matrixDimension) {
        this.id = id;
        this.numberOfComps = numberOfComps;
        this.isMatrixType = true;
        this.matrixDimension = matrixDimension;
    }

    public String getId() {
        return id;
    }

    public int getNumberOfComponents() {
        return numberOfComps;
    }

    public boolean isMatrixType() {
        return isMatrixType;
    }

    public int getMatrixDimension() {
        return matrixDimension;
    }

    public static GLTFAccessorType getById(String id) {
        for(GLTFAccessorType type : GLTFAccessorType.values()) {
            if(type.getId().equals(id))
                return type;
        }

        throw new IllegalArgumentException(id);
    }
}