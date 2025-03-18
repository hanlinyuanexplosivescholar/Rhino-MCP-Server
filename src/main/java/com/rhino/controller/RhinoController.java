package com.rhino.controller;

import com.rhino.model.RhinoObject;
import com.rhino.model.Material;
import com.rhino.model.SceneInfo;
import com.rhino.model.LightInfo;
import com.rhino.service.RhinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rhino")
@RequiredArgsConstructor
@Tag(name = "Rhino MCP Controller", description = "Rhino MCP协议接口")
public class RhinoController {
    
    private final RhinoService rhinoService;
    
    @PostMapping("/connect")
    @Operation(summary = "连接到Rhino")
    public ResponseEntity<String> connect() {
        boolean connected = rhinoService.connectToRhino();
        return ResponseEntity.ok(connected ? "Connected to Rhino" : "Failed to connect to Rhino");
    }
    
    @PostMapping("/command")
    @Operation(summary = "发送命令到Rhino")
    public ResponseEntity<String> sendCommand(@RequestBody @Valid String command) {
        rhinoService.sendCommand(command);
        String response = rhinoService.receiveResponse();
        return ResponseEntity.ok(response != null ? response : "No response received");
    }
    
    @PostMapping("/disconnect")
    @Operation(summary = "断开与Rhino的连接")
    public ResponseEntity<String> disconnect() {
        rhinoService.disconnect();
        return ResponseEntity.ok("Disconnected from Rhino");
    }

    @PostMapping("/objects")
    @Operation(summary = "创建3D对象")
    public ResponseEntity<String> createObject(@RequestBody @Valid RhinoObject object) {
        String objectId = rhinoService.createObject(object);
        return ResponseEntity.ok("Object created with ID: " + objectId);
    }
    
    @PutMapping("/objects/{objectId}")
    @Operation(summary = "修改3D对象")
    public ResponseEntity<String> modifyObject(
            @PathVariable String objectId,
            @RequestBody @Valid RhinoObject object) {
        boolean success = rhinoService.modifyObject(objectId, object);
        return ResponseEntity.ok(success ? "Object modified successfully" : "Failed to modify object");
    }
    
    @DeleteMapping("/objects/{objectId}")
    @Operation(summary = "删除3D对象")
    public ResponseEntity<String> deleteObject(@PathVariable String objectId) {
        boolean success = rhinoService.deleteObject(objectId);
        return ResponseEntity.ok(success ? "Object deleted successfully" : "Failed to delete object");
    }
    
    @GetMapping("/objects/{objectId}")
    @Operation(summary = "获取对象信息")
    public ResponseEntity<RhinoObject> getObject(@PathVariable String objectId) {
        RhinoObject object = rhinoService.getObject(objectId);
        return ResponseEntity.ok(object);
    }

    @PostMapping("/materials")
    @Operation(summary = "创建材料")
    public ResponseEntity<String> createMaterial(@RequestBody @Valid Material material) {
        boolean success = rhinoService.createMaterial(material);
        return ResponseEntity.ok(success ? "Material created successfully" : "Failed to create material");
    }
    
    @PutMapping("/materials/{materialName}")
    @Operation(summary = "修改材料")
    public ResponseEntity<String> modifyMaterial(
            @PathVariable String materialName,
            @RequestBody @Valid Material material) {
        boolean success = rhinoService.modifyMaterial(materialName, material);
        return ResponseEntity.ok(success ? "Material modified successfully" : "Failed to modify material");
    }
    
    @PostMapping("/objects/{objectId}/materials/{materialName}")
    @Operation(summary = "应用材料到对象")
    public ResponseEntity<String> applyMaterialToObject(
            @PathVariable String objectId,
            @PathVariable String materialName) {
        boolean success = rhinoService.applyMaterialToObject(objectId, materialName);
        return ResponseEntity.ok(success ? "Material applied successfully" : "Failed to apply material");
    }
    
    @PutMapping("/objects/{objectId}/color")
    @Operation(summary = "设置对象颜色")
    public ResponseEntity<String> setObjectColor(
            @PathVariable String objectId,
            @RequestBody @Valid double[] color) {
        boolean success = rhinoService.setObjectColor(objectId, color);
        return ResponseEntity.ok(success ? "Color set successfully" : "Failed to set color");
    }
    
    @GetMapping("/objects/{objectId}/material")
    @Operation(summary = "获取对象材料")
    public ResponseEntity<Material> getObjectMaterial(@PathVariable String objectId) {
        Material material = rhinoService.getObjectMaterial(objectId);
        return ResponseEntity.ok(material);
    }

    @GetMapping("/scene")
    @Operation(summary = "获取场景信息")
    public ResponseEntity<SceneInfo> getSceneInfo() {
        SceneInfo sceneInfo = rhinoService.getSceneInfo();
        return ResponseEntity.ok(sceneInfo);
    }
    
    @GetMapping("/scene/layers")
    @Operation(summary = "获取图层列表")
    public ResponseEntity<List<String>> getLayers() {
        List<String> layers = rhinoService.getLayers();
        return ResponseEntity.ok(layers);
    }
    
    @GetMapping("/scene/materials")
    @Operation(summary = "获取材料列表")
    public ResponseEntity<List<String>> getMaterials() {
        List<String> materials = rhinoService.getMaterials();
        return ResponseEntity.ok(materials);
    }
    
    @GetMapping("/scene/objects")
    @Operation(summary = "获取对象列表")
    public ResponseEntity<List<String>> getObjects() {
        List<String> objects = rhinoService.getObjects();
        return ResponseEntity.ok(objects);
    }
    
    @GetMapping("/scene/lights")
    @Operation(summary = "获取光源列表")
    public ResponseEntity<List<LightInfo>> getLights() {
        List<LightInfo> lights = rhinoService.getLights();
        return ResponseEntity.ok(lights);
    }
} 