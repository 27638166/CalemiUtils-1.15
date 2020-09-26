package calemiutils.util.helper;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.network.PacketBuffer;

public class PacketHelper {

    public static Vector3f readVector (PacketBuffer buf) {
        return new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void writeVector (PacketBuffer buf, Vector3f vector) {
        buf.writeFloat(vector.getX());
        buf.writeFloat(vector.getY());
        buf.writeFloat(vector.getZ());
    }
}
