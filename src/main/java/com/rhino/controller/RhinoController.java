package com.rhino.controller;

import com.rhino.model.RhinoObject;
import com.rhino.model.Material;
import com.rhino.model.SceneInfo;
import com.rhino.model.LightInfo;
import com.rhino.service.RhinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rhino")
@RequiredArgsConstructor
public class RhinoController {
    
    private final RhinoService rhinoService;
    
    @PostMapping("/connect")
    public ResponseEntity<String> connect() {
        boolean connected = rhinoService.connectToRhino();
        return ResponseEntity.ok(connected ? "Connected to Rhino" : "Failed to connect to Rhino");
    }
    
    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@RequestBody String command) {
        rhinoService.sendCommand(command);
        String response = rhinoService.receiveResponse();
        return ResponseEntity.ok(response != null ? response : "No response received");
    }
    
    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect() {
        rhinoService.disconnect();
        return ResponseEntity.ok("Disconnected from Rhino");
    }

    @PostMapping("/objects")
    public ResponseEntity<String> createObject(@RequestBody RhinoObject object) {
        String objectId = rhinoService.createObject(object);
        return ResponseEntity.ok("Object created with ID: " + objectId);
    }
    
    @PutMapping("/objects/{objectId}")
    public ResponseEntity<String> modifyObject(
            @PathVariable String objectId,
            @RequestBody RhinoObject object) {
        boolean success = rhinoService.modifyObject(objectId, object);
        return ResponseEntity.ok(success ? "Object modified successfully" : "Failed to modify object");
    }
    
    @DeleteMapping("/objects/{objectId}")
    public ResponseEntity<String> deleteObject(@PathVariable String objectId) {
        boolean success = rhinoService.deleteObject(objectId);
        return ResponseEntity.ok(success ? "Object deleted successfully" : "Failed to delete object");
    }
    
    @GetMapping("/objects/{objectId}")
    public ResponseEntity<RhinoObject> getObject(@PathVariable String objectId) {
        RhinoObject object = rhinoService.getObject(objectId);
        return ResponseEntity.ok(object);
    }

    @PostMapping("/materials")
    public ResponseEntity<String> createMaterial(@RequestBody Material material) {
        boolean success = rhinoService.createMaterial(material);
        return ResponseEntity.ok(success ? "Material created successfully" : "Failed to create material");
    }
    
    @PutMapping("/materials/{materialName}")
    public ResponseEntity<String> modifyMaterial(
            @PathVariable String materialName,
            @RequestBody Material material) {
        boolean success = rhinoService.modifyMaterial(materialName, material);
        return ResponseEntity.ok(success ? "Material modified successfully" : "Failed to modify material");
    }
    
    @PostMapping("/objects/{objectId}/materials/{materialName}")
    public ResponseEntity<String> applyMaterialToObject(
            @PathVariable String objectId,
            @PathVariable String materialName) {
        boolean success = rhinoService.applyMaterialToObject(objectId, materialName);
        return ResponseEntity.ok(success ? "Material applied successfully" : "Failed to apply material");
    }
    
    @PutMapping("/objects/{objectId}/color")
    public ResponseEntity<String> setObjectColor(
            @PathVariable String objectId,
            @RequestBody double[] color) {
        boolean success = rhinoService.setObjectColor(objectId, color);
        return ResponseEntity.ok(success ? "Color set successfully" : "Failed to set color");
    }
    
    @GetMapping("/objects/{objectId}/material")
    public ResponseEntity<Material> getObjectMaterial(@PathVariable String objectId) {
        Material material = rhinoService.getObjectMaterial(objectId);
        return ResponseEntity.ok(material);
    }

    @GetMapping("/scene")
    public ResponseEntity<SceneInfo> getSceneInfo() {
        SceneInfo sceneInfo = rhinoService.getSceneInfo();
        return ResponseEntity.ok(sceneInfo);
    }
    
    @GetMapping("/scene/layers")
    public ResponseEntity<List<String>> getLayers() {
        List<String> layers = rhinoService.getLayers();
        return ResponseEntity.ok(layers);
    }
    
    @GetMapping("/scene/materials")
    public ResponseEntity<List<String>> getMaterials() {
        List<String> materials = rhinoService.getMaterials();
        return ResponseEntity.ok(materials);
    }
    
    @GetMapping("/scene/objects")
    public ResponseEntity<List<String>> getObjects() {
        List<String> objects = rhinoService.getObjects();
        return ResponseEntity.ok(objects);
    }
    
    @GetMapping("/scene/lights")
    public ResponseEntity<List<LightInfo>> getLights() {
        List<LightInfo> lights = rhinoService.getLights();
        return ResponseEntity.ok(lights);
    }
} 