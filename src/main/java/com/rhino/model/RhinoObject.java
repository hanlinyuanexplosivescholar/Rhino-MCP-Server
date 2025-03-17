package com.rhino.model;

import lombok.Data;

@Data
public class RhinoObject {
    private String objectId;
    private String type;  // 对象类型：box, sphere, cylinder等
    private double[] position;  // [x, y, z]
    private double[] size;      // [width, height, depth] 或 [radius, height]等
    private double[] rotation;  // [rx, ry, rz]
    private String material;    // 材质名称
    private String layer;       // 图层名称
} 