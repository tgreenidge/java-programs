/* Adapted from code in "Java Programming", Chapter 20
   by Yakov Fain
   Reference "Java8 Resources" [http://courses.dce.harvard.edu/~cscie55/Java8Resources.html]
 */
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;
//import java.util.stream.Collectors.toList;

public class BeerFest {
    public  static class Beer {
      	public final String name;
      	public final String country;
      	private float price;

    	public Beer(String name, String country,float price){
    	    this.name=name;
    	    this.country=country;
    	    this.price=price;
    	}
	
    	public String toString(){
    	    return "Country: " + country +  " Name: " + name + ", price: " + price;
    	}

    	public float getPrice() {
    	    return price;
    	}

    	public void setPrice(float price) {
    	    this.price = price;
    	}
    }

    //This method returns a list of beers that meet the predicate conditions
    public static List<Beer> beerQuery(List<Beer> beerList, Predicate <Beer> predicate) {
        // ToDo Select Beer's that meet the predicate
        List<Beer> result = beerList.stream()
                            .filter( b -> predicate.test (b) )
                            .collect( Collectors.toList() );
        
        return result;
    }

    static List<Beer> loadCellar(){
        List<Beer> beerStock = new ArrayList<>();

        beerStock.add(new Beer("Stella", "Belgium", 7.75f));
        beerStock.add(new Beer("Sam Adams", "USA", 7.00f));
        beerStock.add(new Beer("Obolon", "Ukraine", 4.00f));
        beerStock.add(new Beer("Bud Light", "USA", 5.00f));
        beerStock.add(new Beer("Yuengling", "USA", 5.50f));
        beerStock.add(new Beer("Leffe Blonde", "Belgium", 8.75f));
        beerStock.add(new Beer("Chimay Blue", "Belgium", 10.00f));
        beerStock.add(new Beer("Brooklyn Lager", "USA", 8.25f));

        return beerStock;
    }

    // This method retuens the beers from a given price range condition(predicate)
    static Predicate<Beer> priceRangeQuery(double low, double high) {
    	// ToDo: compose and return a Predicate that will 
    	//       express the selection criterion
        Predicate<Beer> predicate = new Predicate<Beer>() {
            @Override
            public boolean test (Beer beer) {
                return beer.getPrice() >= low && beer.getPrice() <= high;
            }
        };

        return predicate;
    }

    // This method returns the beers from a given country condition (predicate)
    static Predicate<Beer> countryQuery(String country) {
    	// ToDo: compose and return a Predicate that will 
    	//       express the selection criterion
        Predicate<Beer> predicate = new Predicate<Beer>() {
            @Override
            public boolean test (Beer beer) {
                return beer.country.equals(country);
            }
        };

        return predicate;
    }

    public static void main(String argv[]) {
	List<Beer> beerList = loadCellar();
	// Call beerQuery with a predicate for selecting a country
	beerQuery(beerList, countryQuery("USA")).forEach(System.out::println);
	// Call beerQuery with a predicate for a price range
	beerQuery(beerList, priceRangeQuery(3.00, 7.00)).forEach(System.out::println);
    }
}
