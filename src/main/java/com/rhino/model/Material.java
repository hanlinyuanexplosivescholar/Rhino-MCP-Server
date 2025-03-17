package com.rhino.model;

import lombok.Data;

@Data
public class Material {
    private String name;            // 材料名称
    private String type;            // 材料类型：standard, metal, glass等
    private double[] color;         // RGB颜色值 [0-1]
    private double opacity;         // 不透明度 [0-1]
    private double reflectivity;    // 反射率 [0-1]
    private double roughness;       // 粗糙度 [0-1]
    private double metalness;       // 金属度 [0-1]
    private double clearcoat;       // 清漆度 [0-1]
    private String texture;         // 纹理文件路径
    private double[] textureScale;  // 纹理缩放 [u, v]
    private double[] textureOffset; // 纹理偏移 [u, v]
} 