package com.pratham.chikitse.models;

import java.util.List;

public class Emergency {
    private String id;
    private String name;
    private String nameKannada;
    private String icon;
    private String severity;      // "critical", "warning", "normal"
    private String description;
    private String descriptionKannada;
    private List<Step> steps;

    public Emergency() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameKannada() { return nameKannada; }
    public void setNameKannada(String nameKannada) { this.nameKannada = nameKannada; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionKannada() { return descriptionKannada; }
    public void setDescriptionKannada(String descriptionKannada) { this.descriptionKannada = descriptionKannada; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }
}
