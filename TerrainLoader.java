import java.io.*;
import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;


public class TerrainLoader {
public static byte[] load(String fileName, int mapSize) {
byte pHeightMap[] = new byte[mapSize * mapSize];
FileInputStream input = null;
try {
input = new FileInputStream(fileName);
} catch (IOException e) {
System.out.println(e.getMessage());
}
try {
input.read(pHeightMap, 0, mapSize * mapSize);
input.close();
} catch (IOException e) {
System.out.println(e.getMessage());
}
for (int i = 0; i < pHeightMap.length; i++)
pHeightMap[i] &= 0xFF;
return pHeightMap;
}



public static Texture load(String fileName) {
Texture text = null;
try {
text = TextureIO.newTexture(new File(fileName), false);
text.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER,
GL.GL_NEAREST);
text.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER,
GL.GL_NEAREST);
} catch (Exception e) {
System.out.println(e.getMessage());
System.out.println(fileName);
}
return text;
}
