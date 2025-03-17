package com.rhino.service;

import com.rhino.model.RhinoObject;
import com.rhino.model.Material;
import com.rhino.model.SceneInfo;
import com.rhino.model.LightInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RhinoService {
    
    private static final String RHINO_HOST = "localhost";
    private static final int RHINO_PORT = 8081;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    public boolean connectToRhino() {
        try {
            socket = new Socket(RHINO_HOST, RHINO_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("Successfully connected to Rhino");
            return true;
        } catch (IOException e) {
            log.error("Failed to connect to Rhino: {}", e.getMessage());
            return false;
        }
    }
    
    public void sendCommand(String command) {
        if (out != null) {
            out.println(command);
            log.info("Sent command to Rhino: {}", command);
        } else {
            log.error("Not connected to Rhino");
        }
    }
    
    public String receiveResponse() {
        try {
            if (in != null) {
                String response = in.readLine();
                log.info("Received response from Rhino: {}", response);
                return response;
            }
        } catch (IOException e) {
            log.error("Error receiving response from Rhino: {}", e.getMessage());
        }
        return null;
    }
    
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
                log.info("Disconnected from Rhino");
            }
        } catch (IOException e) {
            log.error("Error disconnecting from Rhino: {}", e.getMessage());
        }
    }

    /**
     * 创建3D对象
     * @param object 3D对象信息
     * @return 创建的对象ID
     */
    public String createObject(RhinoObject object) {
        StringBuilder command = new StringBuilder();
        command.append("_CreateObject ");
        command.append(object.getType()).append(" ");
        command.append(Arrays.toString(object.getPosition())).append(" ");
        command.append(Arrays.toString(object.getSize())).append(" ");
        command.append(Arrays.toString(object.getRotation())).append(" ");
        command.append(object.getMaterial()).append(" ");
        command.append(object.getLayer());
        
        sendCommand(command.toString());
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Object created with ID:")) {
            return response.substring("Object created with ID:".length()).trim();
        }
        throw new RuntimeException("Failed to create object: " + response);
    }
    
    /**
     * 修改3D对象
     * @param objectId 对象ID
     * @param object 新的对象信息
     * @return 是否修改成功
     */
    public boolean modifyObject(String objectId, RhinoObject object) {
        StringBuilder command = new StringBuilder();
        command.append("_ModifyObject ");
        command.append(objectId).append(" ");
        command.append(object.getType()).append(" ");
        command.append(Arrays.toString(object.getPosition())).append(" ");
        command.append(Arrays.toString(object.getSize())).append(" ");
        command.append(Arrays.toString(object.getRotation())).append(" ");
        command.append(object.getMaterial()).append(" ");
        command.append(object.getLayer());
        
        sendCommand(command.toString());
        String response = receiveResponse();
        
        return "Object modified successfully".equals(response);
    }
    
    /**
     * 删除3D对象
     * @param objectId 对象ID
     * @return 是否删除成功
     */
    public boolean deleteObject(String objectId) {
        String command = "_DeleteObject " + objectId;
        sendCommand(command);
        String response = receiveResponse();
        
        return "Object deleted successfully".equals(response);
    }
    
    /**
     * 获取对象信息
     * @param objectId 对象ID
     * @return 对象信息
     */
    public RhinoObject getObject(String objectId) {
        String command = "_GetObject " + objectId;
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Object info:")) {
            // 解析响应字符串为RhinoObject对象
            return parseObjectResponse(response);
        }
        return null;
    }
    
    private RhinoObject parseObjectResponse(String response) {
        // 移除"Object info:"前缀
        String[] parts = response.substring("Object info:".length()).trim().split(" ");
        RhinoObject object = new RhinoObject();
        
        // 解析各个属性
        object.setObjectId(parts[0]);
        object.setType(parts[1]);
        object.setPosition(parseDoubleArray(parts[2]));
        object.setSize(parseDoubleArray(parts[3]));
        object.setRotation(parseDoubleArray(parts[4]));
        object.setMaterial(parts[5]);
        object.setLayer(parts[6]);
        
        return object;
    }
    
    private double[] parseDoubleArray(String arrayStr) {
        // 移除方括号并分割
        String[] values = arrayStr.substring(1, arrayStr.length() - 1).split(",");
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Double.parseDouble(values[i].trim());
        }
        return result;
    }

    /**
     * 创建新材料
     * @param material 材料信息
     * @return 是否创建成功
     */
    public boolean createMaterial(Material material) {
        StringBuilder command = new StringBuilder();
        command.append("_CreateMaterial ");
        command.append(material.getName()).append(" ");
        command.append(material.getType()).append(" ");
        command.append(Arrays.toString(material.getColor())).append(" ");
        command.append(material.getOpacity()).append(" ");
        command.append(material.getReflectivity()).append(" ");
        command.append(material.getRoughness()).append(" ");
        command.append(material.getMetalness()).append(" ");
        command.append(material.getClearcoat()).append(" ");
        command.append(material.getTexture()).append(" ");
        command.append(Arrays.toString(material.getTextureScale())).append(" ");
        command.append(Arrays.toString(material.getTextureOffset()));
        
        sendCommand(command.toString());
        String response = receiveResponse();
        return "Material created successfully".equals(response);
    }
    
    /**
     * 修改材料属性
     * @param materialName 材料名称
     * @param material 新的材料信息
     * @return 是否修改成功
     */
    public boolean modifyMaterial(String materialName, Material material) {
        StringBuilder command = new StringBuilder();
        command.append("_ModifyMaterial ");
        command.append(materialName).append(" ");
        command.append(material.getType()).append(" ");
        command.append(Arrays.toString(material.getColor())).append(" ");
        command.append(material.getOpacity()).append(" ");
        command.append(material.getReflectivity()).append(" ");
        command.append(material.getRoughness()).append(" ");
        command.append(material.getMetalness()).append(" ");
        command.append(material.getClearcoat()).append(" ");
        command.append(material.getTexture()).append(" ");
        command.append(Arrays.toString(material.getTextureScale())).append(" ");
        command.append(Arrays.toString(material.getTextureOffset()));
        
        sendCommand(command.toString());
        String response = receiveResponse();
        return "Material modified successfully".equals(response);
    }
    
    /**
     * 将材料应用到对象
     * @param objectId 对象ID
     * @param materialName 材料名称
     * @return 是否应用成功
     */
    public boolean applyMaterialToObject(String objectId, String materialName) {
        String command = "_ApplyMaterial " + objectId + " " + materialName;
        sendCommand(command);
        String response = receiveResponse();
        return "Material applied successfully".equals(response);
    }
    
    /**
     * 修改对象颜色
     * @param objectId 对象ID
     * @param color RGB颜色值 [0-1]
     * @return 是否修改成功
     */
    public boolean setObjectColor(String objectId, double[] color) {
        String command = "_SetObjectColor " + objectId + " " + Arrays.toString(color);
        sendCommand(command);
        String response = receiveResponse();
        return "Color set successfully".equals(response);
    }
    
    /**
     * 获取对象当前材料
     * @param objectId 对象ID
     * @return 材料信息
     */
    public Material getObjectMaterial(String objectId) {
        String command = "_GetObjectMaterial " + objectId;
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Material info:")) {
            return parseMaterialResponse(response);
        }
        return null;
    }
    
    private Material parseMaterialResponse(String response) {
        String[] parts = response.substring("Material info:".length()).trim().split(" ");
        Material material = new Material();
        
        material.setName(parts[0]);
        material.setType(parts[1]);
        material.setColor(parseDoubleArray(parts[2]));
        material.setOpacity(Double.parseDouble(parts[3]));
        material.setReflectivity(Double.parseDouble(parts[4]));
        material.setRoughness(Double.parseDouble(parts[5]));
        material.setMetalness(Double.parseDouble(parts[6]));
        material.setClearcoat(Double.parseDouble(parts[7]));
        material.setTexture(parts[8]);
        material.setTextureScale(parseDoubleArray(parts[9]));
        material.setTextureOffset(parseDoubleArray(parts[10]));
        
        return material;
    }

    /**
     * 获取场景详细信息
     * @return 场景信息
     */
    public SceneInfo getSceneInfo() {
        String command = "_GetSceneInfo";
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Scene info:")) {
            return parseSceneResponse(response);
        }
        return null;
    }
    
    /**
     * 获取场景中的图层列表
     * @return 图层列表
     */
    public List<String> getLayers() {
        String command = "_GetLayers";
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Layers:")) {
            return parseListResponse(response.substring("Layers:".length()));
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取场景中的材料列表
     * @return 材料列表
     */
    public List<String> getMaterials() {
        String command = "_GetMaterials";
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Materials:")) {
            return parseListResponse(response.substring("Materials:".length()));
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取场景中的对象列表
     * @return 对象列表
     */
    public List<String> getObjects() {
        String command = "_GetObjects";
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Objects:")) {
            return parseListResponse(response.substring("Objects:".length()));
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取场景中的光源列表
     * @return 光源列表
     */
    public List<LightInfo> getLights() {
        String command = "_GetLights";
        sendCommand(command);
        String response = receiveResponse();
        
        if (response != null && response.startsWith("Lights:")) {
            return parseLightsResponse(response.substring("Lights:".length()));
        }
        return new ArrayList<>();
    }
    
    private SceneInfo parseSceneResponse(String response) {
        String[] parts = response.substring("Scene info:".length()).trim().split("\\|");
        SceneInfo scene = new SceneInfo();
        
        // 解析基本信息
        scene.setSceneName(parts[0]);
        scene.setFilePath(parts[1]);
        scene.setViewportSize(parseDoubleArray(parts[2]));
        scene.setCameraPosition(parseDoubleArray(parts[3]));
        scene.setCameraTarget(parseDoubleArray(parts[4]));
        scene.setCameraUp(parseDoubleArray(parts[5]));
        scene.setCameraFov(Double.parseDouble(parts[6]));
        
        // 解析列表信息
        scene.setLayers(parseListResponse(parts[7]));
        scene.setMaterials(parseListResponse(parts[8]));
        scene.setObjects(parseListResponse(parts[9]));
        
        // 解析其他属性
        scene.setSceneBounds(parseDoubleArray(parts[10]));
        scene.setUnits(parts[11]);
        scene.setGridSize(Double.parseDouble(parts[12]));
        scene.setGridVisible(Boolean.parseBoolean(parts[13]));
        scene.setRenderEngine(parts[14]);
        scene.setRenderQuality(parts[15]);
        scene.setAmbientLight(parseDoubleArray(parts[16]));
        scene.setLights(parseLightsResponse(parts[17]));
        
        return scene;
    }
    
    private List<String> parseListResponse(String response) {
        List<String> result = new ArrayList<>();
        if (response != null && !response.trim().isEmpty()) {
            String[] items = response.trim().split(",");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    result.add(item.trim());
                }
            }
        }
        return result;
    }
    
    private List<LightInfo> parseLightsResponse(String response) {
        List<LightInfo> lights = new ArrayList<>();
        if (response != null && !response.trim().isEmpty()) {
            String[] lightStrings = response.trim().split(";");
            for (String lightString : lightStrings) {
                if (!lightString.trim().isEmpty()) {
                    String[] parts = lightString.trim().split(",");
                    LightInfo light = new LightInfo();
                    
                    light.setType(parts[0]);
                    light.setPosition(parseDoubleArray(parts[1]));
                    light.setDirection(parseDoubleArray(parts[2]));
                    light.setColor(parseDoubleArray(parts[3]));
                    light.setIntensity(Double.parseDouble(parts[4]));
                    light.setSpotAngle(Double.parseDouble(parts[5]));
                    light.setEnabled(Boolean.parseBoolean(parts[6]));
                    
                    lights.add(light);
                }
            }
        }
        return lights;
    }
} 