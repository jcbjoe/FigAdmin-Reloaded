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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class FlatFileDatabase extends Database {

    /**
     * 
     * Crappy Flat file database.
     * 
     * @author Serge Humphrey
     * @author yottabyte
     */

    private File banlist;

    int id = 0;

    public boolean initialize(FigAdmin plugin) {

        this.plugin = plugin;
        banlist = new File("plugins/FigAdmin/banlist.txt");
        if (!banlist.exists()) {
            try {
                banlist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeFromBanlist(String UUID) {

        try {

            File tempFile = new File(banlist.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(banlist));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                boolean match = false;
                if (!line.startsWith("#")) {
                    String[] data = line.split("\\|");
                    if (data.length > 1) {
                        match = data[0].equals(UUID) && Integer.parseInt(data[2]) != 2;
                        Bukkit.broadcastMessage("hi"+match);
                    }
                }
                if (!match) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            // Let's delete the old banlist.txt and change the name of our
            // temporary list!
            banlist.delete();
            tempFile.renameTo(banlist);

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void addPlayer(EditBan b) {
        b.id = id++;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(banlist, true));
            writer.write(b.toString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            FigAdmin.log.log(Level.SEVERE, "FigAdmin: Couldn't write to banlist.txt");
        }

    }

    @Override
    public ArrayList<EditBan> getBannedPlayers() {
        id = 0;
        if (!banlist.exists()) {
            try {
                banlist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<EditBan> list = new ArrayList<EditBan>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(banlist));
            String data = null;
            while ((data = in.readLine()) != null) {
                // Checking for blank lines
                if (!data.startsWith("#")) {
                    if (data.length() > 0) {
                        EditBan e = EditBan.loadBan(data);
                        if (e != null && e.type != 2) {
                            list.add(e);
                            id = Math.max(e.id, id);
                        }
                    }

                }
            }
            id++;
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateAddress(String player, String ip) {

        player = player.toLowerCase();
        try {

            File tempFile = new File(banlist.getAbsolutePath() + ".temp");

            BufferedReader br = new BufferedReader(new FileReader(banlist));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String[] data = line.split("\\|");
                    if (data.length > 7) {
                        if (data[0].equals(player)) {
                            data[4] = ip;
                            line = EditBan.loadBan(data).toString();
                        } else if (data[4].equals(ip)) {
                            data[0] = player;
                            line = EditBan.loadBan(data).toString();

                        }
                    }
                }
                pw.println(line);
                pw.flush();

            }
            pw.close();
            br.close();

            // Let's delete the old banlist.txt and change the name of our
            // temporary list!
            banlist.delete();
            tempFile.renameTo(banlist);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected EditBan loadFullRecord(String pName) {
        return loadFullRecord(pName, 0);
    }

    @Override
    protected EditBan loadFullRecord(int id) {
        return loadFullRecord(null, id);
    }

    /*
     * If name == null then it will use the integer id
     */
    private EditBan loadFullRecord(String player, int id) {
        if (player != null)
            player = player.toLowerCase();
        EditBan ban = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(banlist));

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;
                String[] data = line.split("\\|");
                if (data.length > 7) {
                    if (player != null && data[0].equals(player)) {
                        ban = EditBan.loadBan(data);
                        break;
                    } else if (Integer.parseInt(data[1]) == id) {
                        ban = EditBan.loadBan(data);
                        break;
                    }
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ban;
    }

    @Override
    public boolean saveFullRecord(EditBan ban) {
        try {
            File tempFile = new File(banlist.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(banlist));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            boolean written = false;

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;

                if (!written) {
                    String[] data = line.split("\\|");
                    if (data.length > 7) {
                        if (Integer.parseInt(data[1]) == ban.id) {
                            line = ban.toString();
                            written = true;
                        }
                    }
                }
                pw.println(line);
                pw.flush();
            }
            br.close();
            pw.close();

            banlist.delete();
            tempFile.renameTo(banlist);

            return written;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<EditBan> listRecords(String name, boolean exact) {
        name = name.toLowerCase();
        ArrayList<EditBan> bans = new ArrayList<EditBan>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(banlist));

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;
                String[] data = line.split("\\|");
                if (data.length > 7) {

                    if ((exact && data[0].equalsIgnoreCase(name)) || (!exact && data[0].contains(name))) {
                        EditBan ban = EditBan.loadBan(data);
                        if (ban != null) {
                            bans.add(ban);
                            if (bans.size() > 9) {
                                break;
                            }
                        }
                    }
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bans;
    }

    @Override
    protected boolean deleteFullRecord(int id) {
        try {

            File tempFile = new File(banlist.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(banlist));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            // Loops through the temporary file and deletes the player
            while ((line = br.readLine()) != null) {
                boolean match = false;
                if (!line.startsWith("#")) {
                    String[] data = line.split("\\|");
                    if (data.length > 1) {
                        match = Integer.parseInt(data[1]) == id;
                    }
                }
                if (!match) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            // Let's delete the old banlist.txt and change the name of our
            // temporary list!
            banlist.delete();
            tempFile.renameTo(banlist);

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public int getWarnCount(String player) {
        ArrayList<EditBan> records = listRecords(player, true);
        int warns = 0;
        for (EditBan e : records) {
            if (e.type == EditBan.WARN)
                warns++;
        }
        return warns;
    }

    @Override
    public int clearWarnings(String player) {
        int warns = 0;
        try {
            File tempFile = new File(banlist.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(banlist));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            // Loops through the temporary file and deletes the player's
            // warnings
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;

                boolean skip = false;
                EditBan b = EditBan.loadBan(line);
                if (b != null) {
                    if (b.name.equalsIgnoreCase(player) && b.type == EditBan.WARN) {
                        skip = true;
                        warns++;
                    }
                }
                if (!skip)
                    pw.println(line);
                skip = false;
                pw.flush();
            }
            br.close();
            pw.close();

            banlist.delete();
            tempFile.renameTo(banlist);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return warns;
    }

}
