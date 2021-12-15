/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.common;

import java.io.Serializable;
import static proyectop2p.common.MD5.obtenerMD5;

/**
 *
 * @author luis
 */
public class Archivo implements Serializable {

    public Archivo(Id id, String filename, String fullPath) {
        this.id = id;
        this.fullPath = fullPath;
        this.name = filename;
        this.md5 = obtenerMD5(fullPath);
    }

    public Id id;
    public String fullPath;
    public String name;
    public String md5;

    public String toReadbleString() {
        return "De: " + id.id + " fullPath: " + fullPath + " name:" + name + " md5:" + md5;
    }
}
