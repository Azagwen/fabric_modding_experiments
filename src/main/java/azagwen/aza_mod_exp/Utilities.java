package azagwen.aza_mod_exp;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Utilities {

    public static <E> E getHitHalf(Direction hitSide, Vec3d hitPosVec3f, BlockPos pos, boolean invert, E bottomHalf, E topHalf) {
        if (invert) {
            var tempStorage = topHalf;
            topHalf = bottomHalf;
            bottomHalf = tempStorage;
        }

        return hitSide != Direction.UP && (hitSide == Direction.DOWN || !(hitPosVec3f.y - pos.getY() > 0.5D)) ? topHalf : bottomHalf; // hit side isn't UP, and (it's down or superior to 0.5 on Y), then return TOP else return Bottom
    }
}
