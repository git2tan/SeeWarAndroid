package com.example.seawar;
/**
 * Created by Artem on 09.05.2017.
 */
public class GameBoard implements IGameBoard{
    private int[][] board;

    public GameBoard(){
        board = new int [10][10];
    }

    public void unsetShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        if(isHorizontal){
            for(int i = coordX; i < type.ordinal() + 1 + coordX;i++)
                board[i][coordY] = 0;
        }else
        {
            for (int i = coordY; i < type.ordinal()  + 1 + coordY; i++)
                board[coordX][i] = 0;
        }
    }

    public void setShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        int start = 0;
        switch(type){
            case fourDeckShip: start = isHorizontal?4:54; break;
            case threeDeckShip: start = isHorizontal?8:58; break;
            case twoDeckShip: start = isHorizontal?12:62; break;
            case oneDeckShip: start = isHorizontal?15:65; break;
        }

        if(isHorizontal){
            for(int i = coordX; i < type.ordinal() + 1 + coordX;i++)
                board[i][coordY] = start++;
        }else
        {
            for (int i = coordY; i < type.ordinal()  + 1 + coordY; i++)
                board[coordX][i] = start++;
        }
    }

    public void markShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        if(isHorizontal){
            for(int i = coordX; i < type.ordinal() + coordX; i++)
            {
                if(board[i][coordY] <200){
                    board[i][coordY] += 200;
                }
            }

        }else
        {
            for(int i = coordY; i < type.ordinal() + coordX; i++)
            {
                if(board[coordX][i] < 200){
                    board[coordX][i] += 200;
                }
            }
        }
    }

    public int getNumberOfSprite(int coordX, int coordY){
        if(coordX >= 0 && coordX < 10 && coordY >= 0 && coordY < 10)
            return board[coordX][coordY];
        else
            return -1;
    }

    public int[][] getBoard(){
        return board;
    }

    public String toString(){
        String answer = "";
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                answer += board[i][j] == 0?"0":"1";
            }
        }
        return answer;
    }

    public boolean isPossibleToPlaceShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        boolean answer = false;
        boolean isFitHorizontal = false;  //проходит ли по размерам
        boolean isFitVertical = false;
        boolean isFreePlace = false; //места достаточно
        //вычисляем координаты начала и конца
        int coordX1, coordX2, coordY1, coordY2;
        if(isHorizontal) {
            coordX1 = coordX;
            coordY1 = coordY;
            coordX2 = coordX + type.ordinal();
            coordY2 = coordY;
        }
        else{
            coordX1 = coordX;
            coordY1 = coordY;
            coordX2 = coordX;
            coordY2 = coordY + type.ordinal();
        }

        //помещается по горизонтали
        if(coordX1 >= 0 && coordX2 <= 9){
            isFitHorizontal = true;
        }
        else{
            isFitHorizontal = false;
        }
        //то же по вертикали
        if(coordY1 >= 0 && coordY2 <= 9){
            isFitVertical = true;
        }else{
            isFitVertical = false;
        }

        //вычисляем координаты прямоугольника где надо проверять наличие кораблей
        if(isFitVertical && isFitHorizontal) {
            isFreePlace = true;
            int coordRectX1, coordRectX2, coordRectY1, coordRectY2;
            if ((coordX1 - 1) >= 0)
                coordRectX1 = coordX1 - 1;
            else
                coordRectX1 = coordX1;

            if ((coordY1 - 1) >= 0)
                coordRectY1 = coordY1 - 1;
            else
                coordRectY1 = coordY1;

            if (coordX2 + 1 <= 9)
                coordRectX2 = coordX2 + 1;
            else
                coordRectX2 = coordX2;

            if(coordY2 + 1 <= 9)
                coordRectY2 = coordY2 + 1;
            else
                coordRectY2 = coordY2;

            for(int i = coordRectX1; i <=coordRectX2; i++){
                for(int j = coordRectY1; j <= coordRectY2; j++){
                    if(board[i][j] != 0){
                        isFreePlace = false;
                    }
                }
            }
        }
        answer = isFitHorizontal && isFitVertical && isFreePlace;
        return answer;
    }

    public void mark(int coordX, int coordY){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(board[i][j] >= 200)
                    board[i][j] -= 200;
            }
        }
        if(board[coordX][coordY] < 200)
            board[coordX][coordY] += 200;
    }

    public void unMark(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(board[i][j] >= 200)
                    board[i][j] -= 200;
            }
        }
    }

    public void shot(int coordX, int coordY){
        if(board[coordX][coordY]>= 4&&board[coordX][coordY] < 100) {
            board[coordX][coordY] +=100;
        }
        else {
            if(board[coordX][coordY] < 2)
                board[coordX][coordY] = 1;
        }

    }
    public void shot(int coordX, int coordY, boolean isMiss){
        if(isMiss){
            board[coordX][coordY] = 1;
        }
        else {
            board[coordX][coordY] = 2;
        }
    }
    public int markShipAsDestroyed(int coordX, int coordY){
        //к этому времени все ячейки корабля по идее должны быть помечены огнем
        // поэтому остается только пометить соседние ячейки как пустые...
        // вычисляем прямоугольник

        int topLeftX = coordX;
        int topLeftY = coordY;
        int botRightX = coordX;
        int botRightY = coordY;


        while(true){
            if (topLeftX - 1 < 0) {
                break;
            }
            else {
                if ((board[topLeftX - 1][coordY] == 0) || (board[topLeftX - 1][coordY] == 1)){
                    topLeftX = topLeftX - 1;
                    break;
                }
            }
            --topLeftX;
        }

        while(true){
            if (topLeftY - 1 < 0) {
                break;
            }
            else {
                if ((board[coordX][topLeftY - 1] == 0) || (board[coordX][topLeftY - 1] == 1)){
                    topLeftY = topLeftY - 1;
                    break;
                }
            }
            --topLeftY;
        }

        while(true){
            if (botRightX + 1 > 9) {
                break;
            }
            else {
                if ((board[botRightX + 1][coordY] == 0) || (board[botRightX + 1][coordY] == 1)){
                    botRightX = botRightX + 1;
                    break;
                }
            }
            ++botRightX;
        }

        while(true){
            if (botRightY + 1 > 9) {
                break;
            }
            else {
                if ((board[coordX][botRightY + 1] == 0) || (board[coordX][botRightY + 1] == 1)){
                    botRightY = botRightY + 1;
                    break;
                }
            }
            ++botRightY;
        }

        for (int i = topLeftX; i <= botRightX; i++){
            for (int j = topLeftY; j <= botRightY; j++){
                shot(i,j);
            }
        }

        // вычисляем размер корабля который помечен уничтоженным
        int size = 1;
        int topLeftXX = coordX;
        int topLeftYY = coordY;

        while(true){
            if(topLeftXX - 1 >= 0){
                if(board[topLeftXX - 1][coordY] == 2)
                    topLeftXX--;
                else
                    break;
            }
            else
                break;

        }

        while(true){
            if(topLeftYY - 1 >= 0){
                if(board[coordX][topLeftYY - 1] == 2)
                    topLeftYY--;
                else
                    break;
            }
            else
                break;
        }

        int botRightXX = topLeftXX;
        int botRightYY = topLeftYY;
        while(true){
            if(botRightXX + 1 > 9){
                break;
            }
            else{
                if(board[botRightXX + 1][topLeftYY] == 2)
                {
                    botRightXX++;
                    size++;
                }
                else
                    break;
            }
        }

        while(true){
            if(botRightYY + 1 > 9){
                break;
            }
            else{
                if(board[topLeftXX][botRightYY + 1] == 2)
                {
                    botRightYY++;
                    size++;
                }
                else
                    break;
            }
        }
        return size;
    }


    public void actualizeForObs(int [][] actualBoardFromServer){
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                switch(actualBoardFromServer[i][j]){
                    case 0:
                        board[i][j] = 0;
                        break;
                    case 2:
                        board[i][j] = 2;
                        break;
                    case 3:
                        board[i][j] = 1;
                        break;
                }
            }
        }
    }
}
