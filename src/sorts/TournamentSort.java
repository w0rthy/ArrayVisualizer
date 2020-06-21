package sorts;

import java.util.ArrayList;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

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

final public class TournamentSort extends Sort {
    private int[] matches;
    private int tourney;
    
    public TournamentSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Tournament");
        this.setRunAllID("Tournament Sort");
        this.setReportSortID("Tournament Sort");
        this.setCategory("Selection Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private int tourneyCompare(int a, int b) {
        Highlights.markArray(2, a);
        Highlights.markArray(3, b);
        
        Delays.sleep(1);
        
        return Reads.compare(a, b);
    }
    
    private static boolean isPlayer(int i) {
        return i <= 0;
    }
    
    private void setWinner(int root, int winner) {
        Writes.write(matches, root, winner, 0, false, true);
    }
    private void setWinners(int root, int winners) {
        Writes.write(matches, root + 1, winners, 0, false, true);
    }
    private void setLosers(int root, int losers) {
        Writes.write(matches, root + 2, losers, 0, false, true);
    }
    private int getWinner(int root) {
        return matches[root];
    }
    private int getWinners(int root) {
        return matches[root + 1];
    }
    private int getLosers(int root) {
        return matches[root + 2];
    }
    private void setMatch(int root, int winner, int winners, int losers) {
        this.setWinner(root, winner);
        this.setWinners(root, winners);
        this.setLosers(root, losers);
    }
    
    private int getPlayer(int i) {
        return i <= 0 ? Math.abs(i) : this.getWinner(i);
    }
    
	private int pop(int[] arr) {
		int result = arr[this.getPlayer(tourney)];
		tourney = TournamentSort.isPlayer(tourney) ? 0 : this.rebuild(arr, tourney);
		return result;
	}

	private static int makePlayer(int i) {
	    return -i;
	}

	private int makeMatch(int[] arr, int top, int bot, int root) {
        int top_w = this.getPlayer(top);
        int bot_w = this.getPlayer(bot);
        
        if (tourneyCompare(arr[top_w], arr[bot_w]) <= 0)
            this.setMatch(root, top_w, top, bot);
        else
            this.setMatch(root, bot_w, bot, top);
        
        return root;
    }
	
    private int knockout(int[] arr, int i, int k, int root) {
        if (i == k) return TournamentSort.makePlayer(i);
        
        int j = (i + k) / 2;
        return this.makeMatch(arr, this.knockout(arr, i, j, 2 * root), this.knockout(arr,j + 1, k, (2 * root) + 3), root);
    }

    private int rebuild(int[] arr, int root) {
        if (TournamentSort.isPlayer(this.getWinners(root)))
            return this.getLosers(root);
        
        this.setWinners(root, this.rebuild(arr, this.getWinners(root)));
        
        if (tourneyCompare(arr[this.getPlayer(this.getLosers(root))], arr[this.getPlayer(this.getWinners(root))]) < 0) {
            this.setWinner(root, this.getPlayer(this.getLosers(root)));
            
            int temp = this.getLosers(root);
            
            this.setLosers(root, this.getWinners(root));
            this.setWinners(root, temp);
        }
        else {
            this.setWinner(root, this.getPlayer(this.getWinners(root)));
        }
        
        return root;
    }
    
	private void sort(int[] arr, int currentLen) {
		ArrayList<Integer> copy = new ArrayList<>(currentLen);
		for (int i = 0; i < currentLen; i++) {
		    int result = this.pop(arr);
		    
		    Writes.mockWrite(currentLen, copy.size(), result, 0);
		    copy.add(result);
		}

		Highlights.clearAllMarks();

		for (int i = 0; i < currentLen; i++) {
			Integer selected = copy.get(i);
			
			Writes.write(arr, i, selected, 1, false, false);
			Highlights.markArray(1, i);
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.matches = new int[6 * currentLength];
        this.tourney = this.knockout(array, 0, currentLength - 1, 3);
        
        this.sort(array, currentLength);
    }
}