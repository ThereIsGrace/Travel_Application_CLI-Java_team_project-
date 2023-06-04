package art;
/**
 * 
 * 
 * @author 정재은
 *
 */
public class FlightSeat {
	public static final String black    = "\u001B[30m" ;
    public static final String red      = "\u001B[31m" ;
    public static final String green    = "\u001B[32m" ;
    public static final String yellow   = "\u001B[33m" ;
    public static final String blue     = "\u001B[34m" ;
    public static final String purple   = "\u001B[35m" ;
    public static final String cyan     = "\u001B[36m" ;
    public static final String white    = "\u001B[37m" ;
    public static final String exit     = "\u001B[0m" ;
    
	public void showBusinessSeats() {
		System.out.println(red + "                   Business Class");
		System.out.println("  ┌───┐  ┌───┐  ┌───┐          ┌───┐  ┌───┐  ┌───┐");
		System.out.println("    1      2      3              4      5      6  ");
		System.out.println("  └───┘  └───┘  └───┘          └───┘  └───┘  └───┘");
		
		System.out.println("  ┌───┐  ┌───┐  ┌───┐          ┌───┐  ┌───┐  ┌───┐");
		System.out.println("    7      8      9              10     11     12 ");
		System.out.println("  └───┘  └───┘  └───┘          └───┘  └───┘  └───┘");
	}
	
	public void showEconomySeats() {
		System.out.println(green + "                    economy Class");
		int j = 1;
		for(int i = 0; i < 10; i++) {
			System.out.println();
			System.out.println("   ┌───┐┌───┐   ┌───┐┌───┐┌───┐┌───┐   ┌───┐┌───┐ ");
			System.out.print(String.format("%7d%5d%8d%5d%5d%5d%8d%5d\n",j,j+1,
						j+2,j+3,j+4,j+5,j+6,j+7));
			System.out.println("   └───┘└───┘   └───┘└───┘└───┘└───┘   └───┘└───┘ ");
			j+=8;
		}
		System.out.println(exit);
	}
}
