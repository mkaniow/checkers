package checkers; // Ten pakiet jest wymagany - nie usuwaj go
public class EvaluatePosition // Ta klasa jest wymagana - nie usuwaj jej
{
    static private final int WIN=Integer.MAX_VALUE/2;
    static private final int LOSE=Integer.MIN_VALUE/2;
    static private boolean _color; // To pole jest wymagane - nie usuwaj go
    static public void changeColor(boolean color) // Ta metoda jest wymagana - nie zmieniaj jej
    {
        _color=color;
    }
    static public boolean getColor() // Ta metoda jest wymagana - nie zmieniaj jej
    {
        return _color;
    }
    
    static private boolean inBoard(int i, int j) //Funkcja sprawdzająca czy w odległości 2 od wybranego pola jest jeszcze miejsce na planszy
    {
        if(i - 2 < 0 || i + 2 > 7 || j - 2 < 0 || j + 2 > 7)
        {
            return false;
        } 
        return true;
    }
    
    static private int wichState(int i, int j) //Funkcja sprawdzaająca, w której strefie jest pionek
    {
        //Strefa I, przy krawędzi
        if(i == 0 || i == 7 || j == 0 || j == 7)
        {
            return 2;
        }
        //Strefa II
        if((i == 1 && (j !=0 || j !=7)) || (i == 6 && (j !=0 || j !=7)) || (j == 1 && (i !=0 || i !=7)) || (j == 1 && (i !=0 || i !=7)))
        {
            return 1;
        }
        //Strefa III
        return 0;
    }

    static private int inDanger(AIBoard board, int i, int j, boolean white) //Funkcja sprawdzająca czy pionek jest w niebezpieczeństwie
    {
        int side = white ? 1 : -1;
        if(inBoard(i + side, j + 1) && inBoard(i - side, j - 1) && !board._board[i + side][j + 1].empty && board._board[i + side][j + 1].white != white && board._board[i - side][j - 1].empty)
        {
            return -1;
        }
        if(inBoard(i + side, j - 1) && inBoard(i - side, j + 1) && !board._board[i + side][j - 1].empty && board._board[i + side][j - 1].white != white && board._board[i - side][j + 1].empty)
        {
            return -1;
        }
        return 0;
    }

    static private int isCovered(AIBoard board, int i, int j, boolean white) //Funkcja sprawdzająca czy pionek ma wsparcie
    {
        int side = white ? 1 : -1;
        //Sprawdzenie czy znajduje się na obrzeżu
        if(i - 1 < 0 || i + 1 > 7 || j - 1 < 0 || j + 1 > 7)
        {
            return 1;
        }
        //Sprawdzenie czy pionek ma wsparcie z lewej i prawej
        if(!board._board[i - side][j - 1].empty && !board._board[i - side][j + 1].empty)
        {
            return 2;
        }
        if(!board._board[i - side][j - 1].empty || !board._board[i - side][j + 1].empty)
        {
            return 1;
        }
        return -1;
    }

    static private int pawnCapture(AIBoard board, int i, int j, boolean white, int multiKill) //Funkcja sprawdzająca czy pionek może zbić(+ czy może zrobić to kilka razy)
    {
        if(multiKill == 2)
        {
            return multiKill; //Przewidywanie do 2 zbić w przód, bo więcej nie ma sensu zbytnio    
        } 
        int iterResult = multiKill;
        int side = white ? 1 : -1;
        //Sprawdzanie bicia na prawo
        if(inBoard(i + 2 * side, j + 2) && !board._board[i + side][j + 1].empty && board._board[i + side][j + 1].white != white && board._board[i + 2 * side][j + 2].empty)
        {
            iterResult += pawnCapture(board, i + 2 * side, j + 2, board._board[i][j].white, multiKill + 1);
        }
        //Sprawdzanie bicia na lewo
        if(inBoard(i + 2 * side, j - 2) && !board._board[i + side][j - 1].empty && board._board[i + side][j - 1].white != white && board._board[i + 2 * side][j - 2].empty)
        {
            iterResult += pawnCapture(board, i + 2 * side, j - 2, board._board[i][j].white, multiKill + 1);
        }
        return iterResult;
    }
   
    static private int kingCapture(AIBoard board, int i, int j, boolean white, int multiKill, int ban) //Funkcja sprawdzająca czy damka może zbić(+ czy może zrobić to kilka razy)
    {
        if(multiKill == 2)
        {
            return multiKill; //Przewidywanie do 2 zbić w przód, bo więcej nie ma sensu zbytnio    
        } 
        int iterResult = multiKill;
        if(ban == 0)
        {
            //Sprawdzenie bicia do góry w lewą stronę
            if(inBoard(i - 2, j - 2) && !board._board[i - 1][j - 1].empty && white != board._board[i - 1][j - 1].white && board._board[i - 2][j - 2].empty)
            {
                //Odpal jeszcze raz
                iterResult += kingCapture(board, i - 2, j - 2, board._board[i][j].white, multiKill + 1, 1);
            }
            //Sprawdzenie bicia do góry w prawą stronę
            if(inBoard(i - 2, j + 2) && !board._board[i - 1][j + 1].empty && white != board._board[i - 1][j + 1].white && board._board[i - 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i - 2, j + 2, board._board[i][j].white, multiKill + 1, 2);
            }
            //Sprawdzenie bicia do dołu w lewą stronę
            if(inBoard(i + 2, j - 2) && !board._board[i + 1][j - 1].empty && white != board._board[i + 1][j - 1].white && board._board[i + 2][j - 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j - 2, board._board[i][j].white, multiKill + 1, 3);
            }
            //Sprawdzenie bicia do dołu w prawą stronę
            if(inBoard(i + 2, j + 2) && !board._board[i + 1][j + 1].empty && white != board._board[i + 1][j + 1].white && board._board[i + 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j + 2, board._board[i][j].white, multiKill + 1, 4);
            }
            return iterResult;
        }
        if(ban == 1)
        {
            //Sprawdzenie bicia do góry w lewą stronę
            if(inBoard(i - 2, j - 2) && !board._board[i - 1][j - 1].empty && white != board._board[i - 1][j - 1].white && board._board[i - 2][j - 2].empty)
            {
                //Odpal jeszcze raz
                iterResult += kingCapture(board, i - 2, j - 2, board._board[i][j].white, multiKill + 1, 1);
            }
            //Sprawdzenie bicia do góry w prawą stronę
            if(inBoard(i - 2, j + 2) && !board._board[i - 1][j + 1].empty && white != board._board[i - 1][j + 1].white && board._board[i - 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i - 2, j + 2, board._board[i][j].white, multiKill + 1, 2);
            }
            //Sprawdzenie bicia do dołu w lewą stronę
            if(inBoard(i + 2, j - 2) && !board._board[i + 1][j - 1].empty && white != board._board[i + 1][j - 1].white && board._board[i + 2][j - 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j - 2, board._board[i][j].white, multiKill + 1, 3);
            }
            return iterResult;
        }
        if(ban == 2)
        {
            //Sprawdzenie bicia do góry w lewą stronę
            if(inBoard(i - 2, j - 2) && !board._board[i - 1][j - 1].empty && white != board._board[i - 1][j - 1].white && board._board[i - 2][j - 2].empty)
            {
                //Odpal jeszcze raz
                iterResult += kingCapture(board, i - 2, j - 2, board._board[i][j].white, multiKill + 1, 1);
            }
            //Sprawdzenie bicia do góry w prawą stronę
            if(inBoard(i - 2, j + 2) && !board._board[i - 1][j + 1].empty && white != board._board[i - 1][j + 1].white && board._board[i - 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i - 2, j + 2, board._board[i][j].white, multiKill + 1, 2);
            }
            //Sprawdzenie bicia do dołu w prawą stronę
            if(inBoard(i + 2, j + 2) && !board._board[i + 1][j + 1].empty && white != board._board[i + 1][j + 1].white && board._board[i + 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j + 2, board._board[i][j].white, multiKill + 1, 4);
            }
            return iterResult;
        }
        if(ban == 3)
        {
            //Sprawdzenie bicia do góry w lewą stronę
            if(inBoard(i - 2, j - 2) && !board._board[i - 1][j - 1].empty && white != board._board[i - 1][j - 1].white && board._board[i - 2][j - 2].empty)
            {
                //Odpal jeszcze raz
                iterResult += kingCapture(board, i - 2, j - 2, board._board[i][j].white, multiKill + 1, 1);
            }
            //Sprawdzenie bicia do dołu w lewą stronę
            if(inBoard(i + 2, j - 2) && !board._board[i + 1][j - 1].empty && white != board._board[i + 1][j - 1].white && board._board[i + 2][j - 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j - 2, board._board[i][j].white, multiKill + 1, 3);
            }
            //Sprawdzenie bicia do dołu w prawą stronę
            if(inBoard(i + 2, j + 2) && !board._board[i + 1][j + 1].empty && white != board._board[i + 1][j + 1].white && board._board[i + 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j + 2, board._board[i][j].white, multiKill + 1, 4);
            }
            return iterResult;
        }
        if(ban == 4)
        {
            //Sprawdzenie bicia do góry w prawą stronę
            if(inBoard(i - 2, j + 2) && !board._board[i - 1][j + 1].empty && white != board._board[i - 1][j + 1].white && board._board[i - 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i - 2, j + 2, board._board[i][j].white, multiKill + 1, 2);
            }
            //Sprawdzenie bicia do dołu w lewą stronę
            if(inBoard(i + 2, j - 2) && !board._board[i + 1][j - 1].empty && white != board._board[i + 1][j - 1].white && board._board[i + 2][j - 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j - 2, board._board[i][j].white, multiKill + 1, 3);
            }
            //Sprawdzenie bicia do dołu w prawą stronę
            if(inBoard(i + 2, j + 2) && !board._board[i + 1][j + 1].empty && white != board._board[i + 1][j + 1].white && board._board[i + 2][j + 2].empty)
            {
                iterResult += kingCapture(board, i + 2, j + 2, board._board[i][j].white, multiKill + 1, 4);
            }
            return iterResult;
        }
        return 0;
    }
    
    static public int makeTrap(AIBoard board, int i, int j, boolean white)//Stworzenie pułapki
    {
        //pułupka do góry
        if(i == 2 && !board._board[i - 2][j].empty && board._board[i - 2][j].white != white && board._board[i - 2][j].king)
        {
            if(inBoard(i - 1, j - 1) && inBoard(i - 1, j + 1) && board._board[i - 1][j - 1].empty && board._board[i - 1][j + 1].empty)
            {
                return 1;
            }
            if(inBoard(i - 1, j - 1) && board._board[i - 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i - 1, j + 1) && board._board[i - 1][j + 1].empty)
            {
                return 1;
            }
        }
        //pułupka do dołu
        if(i == 5 && !board._board[i + 2][j].empty && board._board[i + 2][j].white != white && board._board[i + 2][j].king)
        {
            if(inBoard(i + 1, j - 1) && inBoard(i + 1, j + 1) && board._board[i + 1][j - 1].empty && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j - 1) && board._board[i + 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j + 1) && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
        }
        //pułupka do lewej
        if(j == 2 && !board._board[i][j - 2].empty && board._board[i][j - 2].white != white && board._board[i][j - 2].king)
        {
            if(inBoard(i - 1, j - 1) && inBoard(i + 1, j - 1) && board._board[i - 1][j - 1].empty && board._board[i + 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i - 1, j - 1) && board._board[i - 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j - 1) && board._board[i + 1][j - 1].empty)
            {
                return 1;
            }
        }
        //pułupka do prawej
        if(j == 5 && !board._board[i][j + 2].empty && board._board[i][j + 2].white != white && board._board[i][j + 2].king)
        {
            if(inBoard(i - 1, j + 1) && inBoard(i + 1, j + 1) && board._board[i - 1][j + 1].empty && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
            if(inBoard(i - 1, j + 1) && board._board[i - 1][j + 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j + 1) && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
        }
        return 0;
    }
    
    static public int advantage(AIBoard board, int i, int j, boolean white)//Bycie przed damką
    {
        //do góry
        if(inBoard(i - 2 , j) && !board._board[i - 2][j].empty && board._board[i - 2][j].white != white && board._board[i - 2][j].king)
        {
            if(inBoard(i - 1, j - 1) && inBoard(i - 1, j + 1) && board._board[i - 1][j - 1].empty && board._board[i - 1][j + 1].empty)
            {
                return 2;
            }
            if(inBoard(i - 1, j - 1) && board._board[i - 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i - 1, j + 1) && board._board[i - 1][j + 1].empty)
            {
                return 1;
            }
        }
        //do dołu
        if(inBoard(i + 2 , j) && !board._board[i + 2][j].empty && board._board[i + 2][j].white != white && board._board[i + 2][j].king)
        {
            if(inBoard(i + 1, j - 1) && inBoard(i + 1, j + 1) && board._board[i + 1][j - 1].empty && board._board[i + 1][j + 1].empty)
            {
                return 2;
            }
            if(inBoard(i + 1, j - 1) && board._board[i + 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j + 1) && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
        }
        //do lewej
        if(inBoard(i , j - 2) && !board._board[i][j - 2].empty && board._board[i][j - 2].white != white && board._board[i - 2][j].king)
        {
            if(inBoard(i - 1, j - 1) && inBoard(i + 1, j - 1) && board._board[i - 1][j - 1].empty && board._board[i + 1][j - 1].empty)
            {
                return 2;
            }
            if(inBoard(i - 1, j - 1) && board._board[i - 1][j - 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j - 1) && board._board[i + 1][j - 1].empty)
            {
                return 1;
            }
        }
        //do prawej
        if(inBoard(i , j + 2) && !board._board[i][j + 2].empty && board._board[i][j + 2].white != white && board._board[i + 2][j].king)
        {
            if(inBoard(i - 1, j + 1) && inBoard(i + 1, j + 1) && board._board[i - 1][j + 1].empty && board._board[i + 1][j + 1].empty)
            {
                return 2;
            }
            if(inBoard(i - 1, j + 1) && board._board[i - 1][j + 1].empty)
            {
                return 1;
            }
            if(inBoard(i + 1, j + 1) && board._board[i + 1][j + 1].empty)
            {
                return 1;
            }
        }
        return 0;
    }

    static public int rushingYards(AIBoard board, int i, int j)//przesuwanie się do przodu
    {
        //Strefa I żółci
        if(board._board[i][j].white && i > 2 && i < 5)
        {
            return 1;
        }
        //Strefa II żółci
        if(board._board[i][j].white && i > 4 && i < 7)
        {
            return 2;
        }
        //Strefa I czerwoni
        if(!board._board[i][j].white && i < 5 && i > 2)
        {
            return 1;
        }
        //Strefa II czerwoni
        if(!board._board[i][j].white && i < 3 && i > 0)
        {
            return 2;
        }
        return 0;
    }
    
    static public int evaluatePosition(AIBoard board)
    {
        int myRating=0;
        int opponentsRating=0;
        int size=board.getSize();
        for (int i=0;i<size;i++)
        {
            for (int j=(i+1)%2;j<size;j+=2)
            {
                if (!board._board[i][j].empty) // pole nie jest puste
                {
                    if (board._board[i][j].white==getColor()) // to jest moj pionek
                    {
                        if (board._board[i][j].king)
                        {
                            myRating += 50;
                            myRating += 7 * kingCapture(board, i, j, board._board[i][j].white, 0, 0);
                            //myRating += 5 * inDanger(board, i, j, board._board[i][j].white);
                            myRating += 8 * advantage(board, i, j, board._board[i][j].white);
                            //myRating += 2 * isCovered(board, i, j, board._board[i][j].white);
                            myRating += 35 * makeTrap(board, i, j, board._board[i][j].white);
                            //myRating += 2 * wichState(i, j);
                        }
                        else
                        {
                            myRating += 20;
                            myRating += 5 * pawnCapture(board, i, j, board._board[i][j].white, 0);
                            myRating += 6 * inDanger(board, i, j, board._board[i][j].white);
                            myRating += 2 * isCovered(board, i, j, board._board[i][j].white);
                            myRating += 4 * rushingYards(board, i, j);
                            myRating += 4 * wichState(i, j);
                        }
                    }
                    else
                    {
                        if (board._board[i][j].king)
                        {
                            opponentsRating += 50;
                            opponentsRating += 7 * kingCapture(board, i, j, board._board[i][j].white, 0, 0);
                            //opponentsRating += 5 * inDanger(board, i, j, board._board[i][j].white);
                            opponentsRating += 8 * advantage(board, i, j, board._board[i][j].white);
                            //opponentsRating += 2 * isCovered(board, i, j, board._board[i][j].white);
                            opponentsRating += 35 * makeTrap(board, i, j, board._board[i][j].white);
                            //opponentsRating += 2 * wichState(i, j);
                        }
                        else
                        {
                            opponentsRating += 20;
                            opponentsRating += 5 * pawnCapture(board, i, j, board._board[i][j].white, 0);
                            opponentsRating += 6 * inDanger(board, i, j, board._board[i][j].white);
                            opponentsRating += 2 * isCovered(board, i, j, board._board[i][j].white);
                            opponentsRating += 4 * rushingYards(board, i, j);
                            opponentsRating += 4 * wichState(i, j);
                        }
                    }
                }
            }
        }
        if (myRating==0) return LOSE; // przegrana
        else if (opponentsRating==0) return WIN; // wygrana
        else return myRating-opponentsRating;
    }
}
