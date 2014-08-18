/*
Copyright (C) 2011 Serge Humphrey

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.btbb.figadmin;

import java.util.ArrayList;

public abstract class Database {

	protected FigAdmin plugin;

	public abstract boolean initialize(FigAdmin plugin);

	/*
	 * Remove a player from banlist
	 */
	public abstract boolean removeFromBanlist(String player);

	
	public abstract void addPlayer(EditBan e);
	

	/*
	 * Get Banned players
	 * Player,Reason
	 */
	public abstract ArrayList<EditBan> getBannedPlayers();
	
	public abstract void updateAddress(String p, String ip);
	
	protected abstract EditBan loadFullRecord(String pName);

    protected abstract EditBan loadFullRecord(int id);
    
    protected abstract boolean deleteFullRecord(int id);
    
    /**
     * 
     * @param name
     * @param exact Exact name or just name contains @name
     * @return
     */
    public abstract ArrayList<EditBan> listRecords(String name, boolean exact);
    
    public abstract boolean saveFullRecord(EditBan ban);
    
    public abstract int getWarnCount(String player);
    
    /**
     * Clears warnings from player
     * 
     * @param player player's exact name
     * 
     * @return How many warnings were deleted
     */
    public abstract int clearWarnings(String player);
	
}