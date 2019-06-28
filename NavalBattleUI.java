import java.util.Scanner;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;

class NavalBattleUI
{
	public static Scanner read = new Scanner(System.in);
	public static NavalBattleBLL logic = new NavalBattleBLL();
	
	public static void main(String[] args)
	{
		int optionMenu = 0;
		
		do
		{
			PrintBeginGame();
			optionMenu = selectOption();
			InitilizeGame(optionMenu);
			
		}while(optionMenu != 2);
	}
	
	public static void PrintWhatWant()
	{
		System.out.println("Digite la opcion que deseea: ");
	}
	
	public static void PrintBeginGame()
	{
		PrintWhatWant();
		System.out.println("1- Iniciar Juego");
		System.out.println("2- Salir");
	}
	
	public static int selectOption()
	{
		int option = 0;
		do
		{
			try
			{
				option = read.nextInt();
			}
			catch (InputMismatchException e)
			{
				option = -1;
				System.out.println("Error: Debe digitar una opcion valida");
				System.out.print("Escoja una opcion ");
				read.next();
			}
		}while(option == -1);
		return option;
	}
	
	public static void InitilizeGame(int option)
	{
		switch(option)
		{
			case 1:
				BeginGame();
				break;

			case 2:
				System.out.println("Saliendo....");
				break;
				
			default:
				System.out.println("Opcion no valida");
				break;
		}
	}
	
	public static void BeginGame()
	{
		int option;
		do
		{
			PrintMenuToPlay();
			option = selectOption();
			boolean end = SelectBoardToPlay(option);
			if(end)
				return;
		}while(option != 4);
	}
	
	public static void PrintMenuToPlay()
	{
		PrintWhatWant();
		System.out.println("1- Tablero 10x10");
		System.out.println("2- Tablero 20x20");
		System.out.println("3- Tablero personalizado");
		System.out.println("4- Salir");
	}
	
	public static boolean SelectBoardToPlay(int option)
	{
		boolean end = false;
		int sizeBoard = 0;
		switch(option)
		{
			case 1:
				sizeBoard = 10;
				boolean importBoard = logic.ImportBoardPlayer(sizeBoard,sizeBoard,"juegoUsuario10");
				if(!importBoard)
				{
					ErrorImportingFile();
					end= true;
					return end;
				}
				System.out.println("Importado correctamente");
				optionsOfPlay(sizeBoard,sizeBoard);
				break;
				
			case 2:
				sizeBoard = 20;
				boolean importBoard20 = logic.ImportBoardPlayer(sizeBoard,sizeBoard,"juegoUsuario20");
				if(!importBoard20)
				{
					ErrorImportingFile();
					end= true;
					return end;
				}
				System.out.println("Importado correctamente");
				optionsOfPlay(sizeBoard,sizeBoard);
				break;
				
			case 3:
				int[] boardCustom = new int[6];
				boolean shipsCreated = true;
				do
				{
					String textPrint = "Digite de cuando quiere que se la x";
					boardCustom[0] = GetNumber(textPrint);
					textPrint = "Digite de cuando quiere que se la y";
					boardCustom[1] = GetNumber(textPrint);
					textPrint = "Digite cuales es el total de barcos";
					boardCustom[2] = GetNumber(textPrint);
					textPrint = "Digite cuales es el total de barcos de 4 posiciones";
					boardCustom[3] = GetNumber(textPrint);
					textPrint = "Digite cuales es el total de barcos de 3 posiciones";
					boardCustom[4] = GetNumber(textPrint);
					textPrint = "Digite cuales es el total de barcos de 2 posiciones";
					boardCustom[5] = GetNumber(textPrint);
					shipsCreated = logic.CreateCustomShip(boardCustom);
					if(!shipsCreated)
						System.out.println("Error");
				}while(!shipsCreated);
				boolean importBoardCustom  = logic.ImportBoardPlayer(boardCustom[0],boardCustom[1],"juegoUsuarioPersonalizado");
				if(!importBoardCustom)
				{
					ErrorImportingFile();
					end= true;
					return end;
				}
				System.out.println("Importado correctamente");
				optionsOfPlay(sizeBoard,sizeBoard);
				break;
				
			case 4:
				System.out.println("Saliendo....");
				break;
				
			default:
				System.out.println("Opcion no valida");
				break;
		}
		return end;
	}
	
	public static int GetNumber(String text)
	{
		System.out.println(text);
		int num = selectOption();
		return num;
	}
	
	public static void ErrorImportingFile()
	{
		System.out.println("No se pudo importar hay errores en el archivo");
	}
	
	public static void optionsOfPlay(int pSize1, int pSize2)
	{
		int option = 0;
		boolean end = false;
		System.out.println(logic.PrintBoard(logic.getBoardComputerUI()));
		GetPointShoot(pSize1,pSize2);
	}
	
	public static void GetPointShoot(int pSize1, int pSize2)
	{
		do
		{
			int x = 0;
			int y = 0;
			do
			{
				x = getPointInX();
				y = getPointInY();
			}while(!logic.noPointOutOfBoard(x,y));
			
			int[][] boardComputerUI = logic.insertShootInBoardComputer(x,y);
			boolean winner = logic.WinnerPlayer();
			if(winner)
			{
				System.out.println(logic.PrintBoard(boardComputerUI));
				System.out.println(logic.StageOfGame(true));
				return;
			}
			int randX = logic.GenerateRandomNumber(pSize1-1);
			int randY = logic.GenerateRandomNumber(pSize2-1);
			int[][] boardPlayerUI = logic.insertShootInBoardPlayer(randX,randY);
			winner = logic.WinnerComputer();
			System.out.println(logic.PrintBoard(boardComputerUI));
			if(winner)
			{
				System.out.println(logic.StageOfGame(true));
				return;
			}
			System.out.println(logic.StageOfGame(false));
		}while(GetOut() != 2);
		System.out.println(logic.PrintBoard(logic.getBoardComputer()));
		System.out.println(logic.StageOfGame(true));
	}
	
	public static int GetOut()
	{
		PrintWhatWant();
		int option = 0;
		do
		{
			System.out.println("1- Continuar");
			System.out.println("2- Salir");
			option = selectOption();
		}while(option < 1  || option > 2);
		return option;
	}
	
	public static int getPointInX()
	{
		System.out.println("Dijite la letra en la coordenada X donde quiere disparar (A/" + logic.lenghtBoard1() + ")");
		char pointX = read.next().toUpperCase().charAt(0);
		return logic.convertChartToInt(pointX);
	}
	
	public static int getPointInY()
	{
		int num = 0;
		do
		{
			System.out.println("Dijite el numero en la coordenada Y donde quiere disparar (0/" + logic.lenghtBoard2() + ")");
			try
			{
				num = read.nextInt();
			}
			catch (InputMismatchException e)
			{
				num = -1;
				System.out.println("Error: Debe ser un numero");
				System.out.print("Dijite el numero en la coordenada Y donde quiere disparar ");
				read.next();
			}
		}while(num == -1);
		
		return num;
	}
}