package com.example.seawar;
/**
 * Created by Artem on 20.05.2017.
 */
public class HimselfGameBoardController implements IClickedController{
    private IController controller;
    public HimselfGameBoardController(IController controller){
        this.controller = controller;
    }
    @Override
    public void mouseClickedSpriteHandler(int coordX, int coordY) {
        controller.mouseClickedSpriteHandler(coordX, coordY);
    }
}
