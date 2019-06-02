package sorts;

import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.clearmarked;
import static array.visualizer.ArrayVisualizer.comps;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.realTimer;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.write;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/*
 * "Some implementations of tournament sort in various languages"
    Copyright (C) 2019 Guy Argo (rugyoga on GitHub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@SuppressWarnings("hiding")
public class TournamentSort<Integer>
{
	Comparator<Integer> comparator;
	Integer[] arr;
	int[] matches;
	int tourney;

	private TournamentSort( Comparator<Integer> comparator, Integer[] v )
	{
		this.arr        = v;
		this.matches    = new int[6*v.length];
		this.comparator = comparator;
		this.tourney    = knockout( 0, v.length-1, 3 );
	}

	private Integer pop()
	{
		Integer result = arr[getPlayer( tourney )];
		tourney = isPlayer( tourney ) ? 0 : rebuild( tourney );
		return result;
	}

	private static <Integer> void sort( int[] arr, Integer[] v, Comparator<Integer>  comparator )
	{
		TournamentSort<Integer> tourney = new TournamentSort<Integer>( comparator, v );
		ArrayList<Integer> copy = new ArrayList<Integer>( v.length );
		for (int i = 0; i < v.length; i++) {
			copy.add( tourney.pop() );
			mockWrite(arr, 0);
		}

		clearmarked();

		for (int i = 0; i < v.length; i++) {
			Integer selected = copy.get(i);
			write(arr, i, (int) selected, 1, false, false);
			marked.set(1, i);
		}
	}

	private int getPlayer( int i )
	{
		return i <= 0 ? Math.abs(i) : getWinner( i );
	}

	private void setWinner( int root, int winner ) { write(matches, root, winner, 0, false, true); }
	private void setWinners( int root, int winners ) { write(matches, root + 1, winners, 0, false, true); }
	private void setLosers( int root, int losers ) { write(matches, root + 2, losers, 0, false, true); }
	private int getWinner( int root )  { return matches[root];   }
	private int getWinners( int root ) { return matches[root+1]; }
	private int getLosers( int root )  { return matches[root+2]; }
	private void setMatch( int root, int winner, int winners, int losers )
	{
		setWinner(root, winner);
		setWinners(root, winners);
		setLosers(root, losers);
	}

	private int mkMatch( int top, int bot, int root )
	{
		int top_w = getPlayer( top );
		int bot_w = getPlayer( bot );
		if (comparator.compare( arr[top_w], arr[bot_w] ) <= 0)
			setMatch( root, top_w, top, bot );
		else
			setMatch( root, bot_w, bot, top );
		return root;
	}

	private int mkPlayer( int i ){ return -i; }

	private int knockout( int i, int k, int root )
	{
		if (i == k) return mkPlayer( i );
		int j = (i+k)/2;
		return mkMatch( knockout( i, j, 2*root ), knockout( j+1, k, 2*root+3 ), root );
	}

	private boolean isPlayer( int i ) { return i <= 0; }

	private int rebuild( int root )
	{
		if (isPlayer( getWinners( root ) ))
			return getLosers( root );
		else
		{
			setWinners( root, rebuild( getWinners( root ) ) );
			if (comparator.compare( arr[getPlayer(getLosers( root ))], arr[getPlayer(getWinners( root ))] ) < 0)
			{
				setWinner( root, getPlayer( getLosers( root ) ) );
				int temp = getLosers( root );
				setLosers( root, getWinners( root ) );
				setWinners( root, temp );
			}
			else
				setWinner( root, getPlayer( getWinners( root ) ) );
			return root;
		}
	}

	static class InstrumentedCompare implements Comparator<Object>
	{
		public int compare( Object a, Object b )
		{
			comps++;
			marked.set(2, (java.lang.Integer) a);
			marked.set(3, (java.lang.Integer) b);
			sleep(1);
			long time = System.nanoTime();
			int cmp = ((java.lang.Integer) a).compareTo( (java.lang.Integer) b);
			if(calcReal) realTimer += (System.nanoTime() - time) / 1e+6;
			return cmp;
		}    	
	}

	public static void tournamentSort(int[] arr)
	{
		Object[] nums = Arrays.stream(arr).boxed().toArray();
		InstrumentedCompare tournamentCompare = new InstrumentedCompare();

		sort( arr, nums, tournamentCompare );
	}
}