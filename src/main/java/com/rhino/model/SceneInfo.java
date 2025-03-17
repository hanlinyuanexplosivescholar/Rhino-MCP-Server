package com.rhino.model;

import lombok.Data;
import java.util.List;

@Data
public class SceneInfo {
    private String sceneName;           // 场景名称
    private String filePath;            // 文件路径
    private double[] viewportSize;      // 视口大小 [width, height]
    private double[] cameraPosition;    // 相机位置 [x, y, z]
    private double[] cameraTarget;      // 相机目标点 [x, y, z]
    private double[] cameraUp;          // 相机上方向 [x, y, z]
    private double cameraFov;           // 相机视场角
    private List<String> layers;        // 图层列表
    private List<String> materials;     // 材料列表
    private List<String> objects;       // 对象列表
    private double[] sceneBounds;       // 场景边界 [minX, minY, minZ, maxX, maxY, maxZ]
    private String units;               // 单位系统
    private double gridSize;            // 网格大小
    private boolean isGridVisible;      // 网格是否可见
    private String renderEngine;        // 渲染引擎
    private String renderQuality;       // 渲染质量
    private double[] ambientLight;      // 环境光 [r, g, b]
    private List<LightInfo> lights;     // 光源列表
}

@Data
class LightInfo {
    private String type;                // 光源类型：point, spot, directional
    private double[] position;          // 位置 [x, y, z]
    private double[] direction;         // 方向 [x, y, z]
    private double[] color;             // 颜色 [r, g, b]
    private double intensity;           // 强度
    private double spotAngle;           // 聚光灯角度
    private boolean isEnabled;          // 是否启用
} 