package com.pratham.chikitse.models;

public class Step {
    private int stepNumber;
    private String title;
    private String titleKannada;
    private String instruction;
    private String instructionKannada;
    private String doText;
    private String doTextKannada;
    private String dontText;
    private String dontTextKannada;
    private String drawableIcon;   // name of drawable resource

    public Step() {}

    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleKannada() { return titleKannada; }
    public void setTitleKannada(String titleKannada) { this.titleKannada = titleKannada; }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }

    public String getInstructionKannada() { return instructionKannada; }
    public void setInstructionKannada(String instructionKannada) { this.instructionKannada = instructionKannada; }

    public String getDoText() { return doText; }
    public void setDoText(String doText) { this.doText = doText; }

    public String getDoTextKannada() { return doTextKannada; }
    public void setDoTextKannada(String doTextKannada) { this.doTextKannada = doTextKannada; }

    public String getDontText() { return dontText; }
    public void setDontText(String dontText) { this.dontText = dontText; }

    public String getDontTextKannada() { return dontTextKannada; }
    public void setDontTextKannada(String dontTextKannada) { this.dontTextKannada = dontTextKannada; }

    public String getDrawableIcon() { return drawableIcon; }
    public void setDrawableIcon(String drawableIcon) { this.drawableIcon = drawableIcon; }
}
