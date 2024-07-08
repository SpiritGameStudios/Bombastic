package dev.callmeecho.bombastic.main.utils;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
// todo: add this fix to cabinetapi
public class VoxelShapeHelperFix {
    public static VoxelShape flipY(VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        shape.forEachBox(((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(
                buffer[1],
                VoxelShapes.cuboid(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ),
                BooleanBiFunction.OR
        )));

        return buffer[1];
    }

    public static VoxelShape rotateZ(VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        shape.forEachBox(((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(
                buffer[1],
                VoxelShapes.cuboid(minY, minX, minZ, maxY, maxX, maxZ),
                BooleanBiFunction.OR
        )));

        return buffer[1];
    }
}
