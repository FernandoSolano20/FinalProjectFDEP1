import java.util.Scanner;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NavalBattleBLL
{
	private static NavalBattleDAL data = new NavalBattleDAL();
	private static int countShipOfPlayer = 0;
	
	private static void CreateBoardComputer(int pSizeBoard1, int pSizeBoard2)
	{
		data.sizeOfBoard(pSizeBoard1,pSizeBoard2);
		CreateBoard(pSizeBoard1,pSizeBoard2);
		data.exportBoardComputer();
	}
	/*public*/
	public static char lenghtBoard1()
	{
		return (char)(65 + data.lengthBoards(true) -1) ;
	}
	/*public*/
	public static int lenghtBoard2()
	{
		return data.lengthBoards(false) - 1;
	}
	
	private static void CreateShipsToDefaultsBoards(int pSizeBoard)
	{
		int numberShips = 0;
		int pattern = 0;
		int sizeShip = 5;
		int numberShipInBoard = 0;
		
		if(pSizeBoard == 10)
		{
			numberShips = 6;
			pattern = 2;
		}
		else if(pSizeBoard == 20)
		{
			numberShips = 12;
			pattern = 4;
		}
		data.InitializeNumberShips(numberShips);
		for(int index = 0; index < numberShips; index++)
		{
			if(index % pattern == 0)
			{
				numberShipInBoard++;
				sizeShip--;
			}
			data.insertShipInArray(sizeShip,numberShipInBoard);
		}
	}
	/*public*/
	public static boolean CreateCustomShip(int[] customBoard)
	{
		if((customBoard[0] * customBoard[1]) <  (4 * customBoard[3] + 3 * customBoard[4] + 2 * customBoard[5]))
		{
			return false;
		}
		else if(customBoard[2] != (customBoard[3] + customBoard[4] + customBoard[5]))
		{
			return false;
		}
		data.InitializeNumberShips(customBoard[2]);
		int num = 1;
		int length = 4;
		for(int i = 3; i <= 5; i++)
    	{
			for(int j = 0; j < customBoard[i]; j ++)
    		{
    			data.insertShipInArray(length,num);
    		}
			num++;
			length--;
		}
		return true;
	}
	
	private static void CreateBoard(int pSizeBoard1, int pSizeBoard2)
	{
		for(int i = 0; i < data.lengthShips(); i++)
		{
			createBoard(pSizeBoard1, pSizeBoard2, data.getShip(i));
		}
	}
	/*public*/
	public static int[][] getBoardComputerUI()
	{
		return data.getAllBoardComputerUI();
	}
	/*public*/
	public static int[][] getBoardComputer()
	{
		return data.getAllBoardComputer();
	}
	
	private static int[][] getBoardPlayer()
	{
		return data.getAllBoardPlayer();
	}
	/*public*/
	public static int GenerateRandomNumber(int pLimit)
	{
		int numRand = (int)(Math.random()*((pLimit-0)+1))+0;
		return numRand;
	}
	
	private static void createBoard(int sizeBoard1, int sizeBoard2, int[] lengthShip)
	{
		boolean putShipInBoard = false;
		int[] positions = new int[4];
		do 
		{
			int posiRand1 = GenerateRandomNumber(sizeBoard1-1);
			int posiRand2 = GenerateRandomNumber(sizeBoard2-1);
			int direction = GenerateRandomNumber(3);
			
			if(direction == 0)
			{
				positions = ShipToRight(posiRand1,posiRand2,lengthShip[0]);
				putShipInBoard = validateShip(positions, lengthShip[1]);
			}
			else if(direction == 1)
			{
				positions = ShipToBottom(posiRand1,posiRand2,lengthShip[0]);
				putShipInBoard = validateShip(positions, lengthShip[1]);
			}
			else if(direction == 2)
			{
				positions = ShipToTop(posiRand1,posiRand2,lengthShip[0]);
				putShipInBoard = validateShip(positions, lengthShip[1]);
			}
			else if(direction == 3)
			{
				positions = ShipToLeft(posiRand1,posiRand2,lengthShip[0]);
				putShipInBoard = validateShip(positions, lengthShip[1]);
			}
		}while(putShipInBoard == false);
		
		data.insertShipInBoard(positions, lengthShip[1]);
	}
	
	private static int[] ShipToRight(int x, int y, int length)
	{
		int[]position = new int[6];
		position[0] = x;
		position[1] = y;
		position[2] = x;
		position[3] = y+length-1;
		position[4] = x;
		position[5] = y+1;
		return position;
	}
	
	private static int[] ShipToBottom(int x, int y, int length)
	{
		int[]position = new int[6];
		position[0] = x;
		position[1] = y;
		position[2] = x+length-1;
		position[3] = y;
		position[4] = x+1;
		position[5] = y;
		return position;
	}
	
	private static int[] ShipToTop(int x, int y, int length)
	{
		int[]position = new int[4];
		position[0] = x-(length-1);
		position[1] = y;
		position[2] = x;
		position[3] = y;
		return position;
	}
	
	private static int[] ShipToLeft(int x, int y, int length)
	{
		int[]position = new int[4];
		position[0] = x;
		position[1] = y-(length-1);
		position[2] = x;
		position[3] = y;
		return position;
	}
	
	private static boolean validateShip(int[] positions, int lengthActualShip)
	{
		if(noShipOutOfBoard(positions) && noShipUnderneathOther(positions) && noShipSameSizeAround(positions, lengthActualShip, data.getAllBoardComputer()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static boolean noShipOutOfBoard(int[] positions)
	{		
		if(noPointOutOfBoard(positions[0],positions[1]) && noPointOutOfBoard(positions[2],positions[3]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static boolean noShipUnderneathOther(int[] positions)
	{
		boolean result = true;
		for(int i = positions[0]; i <= positions[2]; i++)
		{
			for(int j = positions[1]; j <= positions[3]; j++)
			{
				if(data.getSpaceInComputerBoard(i,j) != 0)
				{
					result = false;
					return result;
				}
			}
		}
		return result;
	}
	
	private static boolean noShipSameSizeAround(int[] positions, int lengthActualShip, int[][] board)
	{
		boolean result = true;
		
		int beginLoop1 = getBeginNumberOfLoop(positions[0]-1);
		int endLoop1 = getBeginNumberOfLoop(positions[1]-1);
		int beginLoop2 = getEndNumberOfLoop(positions[2]+1, data.lengthBoards(true));
		int endLoop2 = getEndNumberOfLoop(positions[3]+1, data.lengthBoards(false));
		
		for(int i = beginLoop1; i <= beginLoop2; i++)
		{
			for(int j = endLoop1; j <= endLoop2; j++)
			{
				if(i < positions[0] || i > positions[2] || j < positions[1] || j > positions[3])
				{
					if(board[i][j] == lengthActualShip)
					{
						result = false;
						return result;
					}
				}
			}
		}
		return result;
	}
	
	private static int getBeginNumberOfLoop(int numberIndexLoop)
	{
		if(numberIndexLoop < 0)
		{
			return 0;
		}
		return numberIndexLoop;
	}
	
	private static int getEndNumberOfLoop(int numberIndexLoop, int lengthArray)
	{
		if(numberIndexLoop >= lengthArray)
		{
			return 9;
		}
		return numberIndexLoop;
	}
	/*public*/
	public static boolean ImportBoardPlayer(int size1, int size2, String filePlayer)
	{
		countShipOfPlayer = 0;
		int shipsBoards = 0;
		data.InitializeBoardPlayer(size1,size2);
		if(size1 == 10 && size2 == 10 || size1 == 20 && size2 == 20)
		{
			CreateShipsToDefaultsBoards(size1);
		}
		int[][] boardPlayer = data.importBoardPlayer(size1,size2,filePlayer);
        int numLength = 0;
        for(int in =0; in < boardPlayer.length; in++)
        {
           	for(int j = 0; j < boardPlayer[0].length; j++)
           	{
           		if(boardPlayer[in][j] != 0)
           		{
           			shipsBoards++;
           			if(boardPlayer[in][j] == 3)
           			{
           				if(getPoint(ShipToRight(in,j,2),2,boardPlayer) == -1)
           				{
           					return false;
           				}
           				else if(getPoint(ShipToBottom(in,j,2),2,boardPlayer) == -1)
           				{
           					return false;
           				}
           			}
           			else if(boardPlayer[in][j] == 2)
           			{
           				if(getPoint(ShipToRight(in,j,3),3,boardPlayer) == -1)
           				{
           					return false;
           				}
           				else if(getPoint(ShipToBottom(in,j,3),3,boardPlayer) == -1)
           				{
           					return false;
           				}
           			}
           			else if(boardPlayer[in][j] == 1)
           			{
           				if(getPoint(ShipToRight(in,j,4),4,boardPlayer) == -1)
           				{
           					return false;
           				}
           				else if(getPoint(ShipToBottom(in,j,4),4,boardPlayer) == -1)
           				{
           					return false;
           				}
           			}
           		}
           	}
        }
        if(shipsBoards == data.getNumberPositionsShips())
        {
        	data.createBoardPlayer(boardPlayer);
        	CreateBoardComputer(size1,size2);
        	return true;  
        }
        return false;
	}
	
	private static int getPoint(int[] points, int lengthShip, int[][]board)
	{
		if(!noShipOutOfBoard(points))
		{
			return 0;
		}
		else
		{
			if(board[points[0]][points[1]] == board[points[2]][points[3]] && noPointOutOfBoard(points[4],points[5]) && board[points[0]][points[1]] == board[points[4]][points[5]])
			{
				if(noShipSameSizeAround(points, board[points[0]][points[1]], board))
				{
					countShipOfPlayer++;
					if(countShipOfPlayer > data.lengthShips())
					{
						return -1;
					}
					return 1;
				}	
			}
			else
			{
				return 2;
			}
		}
		return -1;
	}
	/*public*/
	public static int convertChartToInt(char letter)
	{
		return ((int)letter-65);
	}
	/*public*/
	public static boolean noPointOutOfBoard(int x, int y)
	{
		int board1 = data.lengthBoards(true);
		int board2 = data.lengthBoards(false);
		
		if((board2 > y && y >= 0) && (board1 > x && x >= 0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/*public*/
	public static int[][] insertShootInBoardComputer(int x, int y)
	{
		int[][] boardComputer = data.getAllBoardComputer();
		int[][] boardComputerUI = data.getAllBoardComputerUI();
		insertShoot(x,y,boardComputer,boardComputerUI);
		data.UpdateBoardComputers(boardComputer,boardComputerUI);
		return getBoardComputerUI();
	}
	/*public*/
	public static int[][] insertShootInBoardPlayer(int x, int y)
	{
		int[][] boardPlayer = data.getAllBoardPlayer();
		insertShoot(x,y,boardPlayer,boardPlayer);
		data.UpdateBoardPlayer(boardPlayer);
		return getBoardPlayer();
	}
	
	private static void insertShoot(int x, int y, int[][] boardDAL, int[][] boardUI)
	{
		int[] pointsShip = {0,0,-1,-1};
		if(boardDAL[x][y] == 0)
		{
			boardDAL[x][y] = 9;
			boardUI[x][y] = 9;
		}
		else if(boardDAL[x][y] == 1)
		{
			pointsShip = getShipToDestroy(x,y,boardDAL,4);
		}
		else if(boardDAL[x][y] == 2)
		{
			pointsShip = getShipToDestroy(x,y,boardDAL,3);
		}
		else if(boardDAL[x][y] == 3)
		{
			pointsShip = getShipToDestroy(x,y,boardDAL,2);
		}
		
		for(int i = pointsShip[0]; i <= pointsShip[2]; i++)
		{
			for(int j = pointsShip[1]; j <= pointsShip[3]; j++)
			{
				boardDAL[i][j] = -1;
				boardUI[i][j] = -1;
			}
		}
	}
	
	private static int[] getShipToDestroy(int x, int y, int[][] board, int lengthShip)
	{
		int[] points = new int[4];
		if(noPointOutOfBoard(x,y-1) && board[x][y] == board[x][y-1])
		{
			do
			{
				y--;
			}while(noPointOutOfBoard(x,y-1) && board[x][y] == board[x][y-1]);
		}
		
		if(noPointOutOfBoard(x-1,y) && board[x][y] == board[x-1][y])
		{
			do
			{
				x--;
			}while(noPointOutOfBoard(x-1,y) && board[x][y] == board[x-1][y]);
		}
		
		if(noPointOutOfBoard(x,y+1) && board[x][y] == board[x][y+1])
		{
			points = ShipToRight(x,y,lengthShip);
		}
		
		if(noPointOutOfBoard(x+1,y) && board[x][y] == board[x+1][y])
		{
			points = ShipToBottom(x,y,lengthShip);
		}
		
		return points;
	}
	/*public*/
	public static boolean WinnerPlayer()
	{
		int[][] boardComputer = getBoardComputerUI();
		return Winnner(boardComputer);
	}
	/*public*/
	public static boolean WinnerComputer()
	{
		int[][] boardPlayer = getBoardPlayer();
		return Winnner(boardPlayer);
	}
	
	private static boolean Winnner(int[][] pBoard)
	{
		int destroyedShips = NumberOfDestroyShips(pBoard);
		if(data.getNumberPositionsShips() == destroyedShips)
		{
			return true;
		}
		return false;
	}
	
	private static int NumberOfDestroyShips(int[][] pBoard)
	{
		int destroyedShips = 0;
		for(int i = 0; i < pBoard.length; i++)
		{
			for(int j = 0; j < pBoard[0].length; j++)
			{
				if(pBoard[i][j] == -1)
				{
					destroyedShips++;
				}
			}
		}
		return destroyedShips;
	}
	/*public*/
	public static String StageOfGame(boolean ended)
	{
		String printer = "";
		int[][] pBoardComp = getBoardComputerUI();
		int[][] pBoardPlay = getBoardPlayer();
		int destroyedShipsComp = NumberOfDestroyShips(pBoardComp);
		int destroyedShipsPlay = NumberOfDestroyShips(pBoardPlay);
		if(destroyedShipsComp == destroyedShipsPlay)
		{
			printer = "Empate \r\n";
			printer += "Puntos computadora: " + destroyedShipsPlay + "\r\n";
			printer += "Puntos Usuario: " + destroyedShipsComp;

		}
		else if(destroyedShipsComp > destroyedShipsPlay)
		{
			if(!ended)
				printer = "Vas ganando la partida \r\n";
			else
				System.out.println("Gano la partida");
			printer += "Puntos computadora: " + destroyedShipsPlay + "\r\n";
			printer += "Puntos Usuario: " + destroyedShipsComp;
		}
		else if(destroyedShipsComp < destroyedShipsPlay)
		{
			if(!ended)
				printer = "Vas perdiendo la partida \r\n";
			else
				System.out.println("Perdio la partida");
			printer += "Puntos computadora: " + destroyedShipsPlay + "\r\n";
			printer += "Puntos Usuario: " + destroyedShipsComp;
		}
		return printer;
	}
	/*public*/
	public static String PrintBoard(int[][] pBoardComputer)
	{
		int[][] pBoardPlayer = getBoardPlayer();
		char[] posX = new char[26];
		int number = 0;
		String printer = "";
		for(char letter = 'A'; letter <= 'Z'; letter++)
		{
			posX[number] = letter;
			number++;
		}
		
		int contLetter = 0;
		printer = "       Computadora                        Jugador\r\n";
		printer += "   ";
		for(int index = 0; index < pBoardPlayer[0].length; index++)
		{
			printer += index + " ";
		}
		
		printer += "            ";
		for(int index = 0; index < pBoardPlayer[0].length; index++)
		{
			printer += index + " ";
		}
		printer += "\r\n";
		printer += "\r\n";
		for(int i = 0; i < pBoardComputer.length; i++)
		{
			printer += posX[contLetter] + "  ";
			for(int j = 0; j < pBoardComputer[0].length; j++)
			{
				if(pBoardComputer[i][j] != -1)
				{
					printer += pBoardComputer[i][j] + " ";
				}
				else
				{
					printer += "x ";	
				}
			}
			printer += "         " + posX[contLetter] + "  ";
			contLetter++;
			for(int j2 = 0; j2 < pBoardPlayer[0].length; j2++)
			{
				if(pBoardPlayer[i][j2] != -1)
				{
					printer += pBoardPlayer[i][j2] + " ";
				}
				else
				{
					printer += "x ";	
				}
			}
			printer += "\r\n";
		}
		return printer;
	}
}