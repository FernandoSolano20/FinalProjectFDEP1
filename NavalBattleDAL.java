import java.util.Scanner;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NavalBattleDAL
{
	private static int[][] boardComputer;
	private static int[][] boardComputerUI;
	private static int[][] boardPlayer;
	private static int[][] lengthShip;
	private static int contShip;
	
	public static void sizeOfBoard(int sizeBoard, int sizeBoard0)
	{
		boardComputer = new int [sizeBoard][sizeBoard0];
		boardComputerUI = new int [sizeBoard][sizeBoard0];
	}
	
	public static void InitializeBoardPlayer(int pSize1, int pSize2)
	{
		boardPlayer = new int[pSize1][pSize2];
	}
	
	public static void InitializeNumberShips(int numberShips)
	{
		lengthShip = new int[numberShips][2];
		contShip = 0;
	}
	
	public static int lengthBoards(boolean pLength1)
	{
		if(pLength1)
		{
			return boardPlayer.length;
		}
		else
		{
			return boardPlayer[0].length;
		}
	}
	
	public static int lengthShips()
	{
		return lengthShip.length;
	}
	
	public static void insertShipInArray(int pLength, int pNumber)
	{
		lengthShip[contShip][0] = pLength;
		lengthShip[contShip][1] = pNumber;
		contShip++;
	}
	
	public static int[] getShip(int pIndice)
	{
		return lengthShip[pIndice];
	}
	
	public static int getNumberPositionsShips()
	{
		int numberPositions = 0;
		for(int i = 0; i < lengthShip.length; i++)
		{
			numberPositions += lengthShip[i][0];
		}
		return numberPositions;
	}
	
	public static void insertShipInBoard(int[]points, int pLengthShip)
	{
		for(int i= points[0]; i <= points[2]; i++)
		{
			for(int j= points[1]; j <= points[3]; j++)
			{
				boardComputer[i][j] = pLengthShip;
			}
		}
	}
	
	public static int getSpaceInComputerBoard(int pIndex1, int pIndex2)
	{
		return boardComputer[pIndex1][pIndex2];
	}
	
	public static int[][] getAllBoardComputer()
	{
		return boardComputer;
	}
	
	public static int[][] getAllBoardComputerUI()
	{
		return boardComputerUI;
	}
	
	public static int[][] getAllBoardPlayer()
	{
		return boardPlayer;
	}
	
	public static void exportBoardComputer()
	{
		File file = null;
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try 
		{
			file = new File("C:\\Users\\luisf\\Desktop\\FinalProjectFDP1-master\\txt\\juegoComputadora.txt");
			fileWriter = new FileWriter(file);
			// create file if not exists
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			// initialize BufferedWriter
			writer = new BufferedWriter(fileWriter);

			//write integers
			for(int i = 0; i < boardComputer.length; i++)
			{
				for(int j = 0; j < boardComputer[0].length; j++)
				{
					writer.write((char)(boardComputer[i][j]+'0'));
					writer.write(' ');
				}
				writer.append(System.getProperty("line.separator"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			// close BufferedWriter
			if (writer != null) 
			{
				try 
				{
					writer.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			// close FileWriter
			if (fileWriter != null) 
			{
				try 
				{
					fileWriter.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int[][] importBoardPlayer(int size1, int size2, String file)
	{
		int[][] board = new int[size1][size2];
		try
		{
			FileInputStream fin=new FileInputStream("C:\\Users\\luisf\\Desktop\\FinalProjectFDP1-master\\txt\\" + file + ".txt");    
           	int i;
           	int index1 = 0;
           	int index2 = 0;
           	while ((i = fin.read()) != -1) 
           	{
           		char letter = (char) i;
           		if(index2 == board[0].length)
           		{
           			index2 = 0;
           			index1++;
           		}
           		if(letter != ';')
           		{
           			if(Character.getNumericValue(letter) != -1)
	           		{
	           			board[index1][index2] = Character.getNumericValue(letter);
	           			index2++;
	           		}	
           		}	
           	}
           	fin.close();
        }
        catch(Exception e)
        {
        	System.out.println(e);
        }
        return board;
	}
	
	public static void createBoardPlayer(int[][] board)
	{
		boardPlayer = board;
	}
	
	public static void UpdateBoardComputers(int[][] pBoard, int[][] pBoardUI)
	{
		boardComputer = pBoard;
		boardComputerUI = pBoardUI;
	}
	
	public static void UpdateBoardPlayer(int[][] pBoard)
	{
		boardPlayer = pBoard;
	}
}