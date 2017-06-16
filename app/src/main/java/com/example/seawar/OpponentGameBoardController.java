package com.example.seawar;
/**
 * Created by Artem on 20.05.2017.
 */
public class OpponentGameBoardController implements IClickedController {
    private IController controller;
    public OpponentGameBoardController(IController controller){
        this.controller = controller;
    }

    @Override
    public void mouseClickedSpriteHandler(int coordX, int coordY) {
        controller.mouseClickedSpriteOpponentHandler(coordX, coordY);
    }
}
