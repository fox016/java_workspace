package hacker;

import java.util.Scanner;

public class Scheduling
{
	public static void run(Scanner scanner)
	{
		int count = Integer.parseInt(scanner.nextLine());
		
		int[] timeNeed = new int[count];
		int[] timeDue = new int[count];
		for(int i = 0; i < count; i++)
		{
			String[] input = scanner.nextLine().split(" ");
			timeNeed[i] = Integer.parseInt(input[0]);
			timeDue[i] = Integer.parseInt(input[1]);
			
			if(timeDue[i] > timeNeed[i])
			{
				System.out.println("impossible");
				return;
			}
		}
		
		int timeTaken = 0;
		int iter = 0;
		while(iter != count)
		{
			iter++;
		}
	}

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		int cases = Integer.parseInt(scanner.nextLine());
		for(int i = 0; i < cases; i++) {
			run(scanner);
		}
	}
}
