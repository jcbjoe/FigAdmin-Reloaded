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

public class EditBan {

    int id;
    String UUID;
    String name;
    String reason;
    String admin;
    String IP;
    long time;
    long endTime;
    int type;
    final static int BAN = 0;
    static final int IPBAN = 1;
    final static int WARN = 2;

    EditBan(int id, String victimUUID, String name, String reason, String admin, long time, long endTime, int type, String IP) {
        this.id = id;
        this.UUID = victimUUID;
        this.name = name.toLowerCase();
        this.reason = reason;
        this.admin = admin;
        this.time = time;
        this.endTime = endTime;
        this.type = type;
        this.IP = IP;
    }

    EditBan(String victimUUID, String name, String reason, String admin, int type) {
        this.id = 0;
        this.UUID = victimUUID;
        this.name = name.toLowerCase();
        this.reason = reason;
        this.admin = admin;
        this.time = System.currentTimeMillis();
        this.endTime = 0;
        this.type = type;
        this.IP = null;
    }

    EditBan(String victimUUID, String name, String reason, String admin, String IP, int type) {
        this.id = 0;
        this.UUID = victimUUID;
        this.name = name.toLowerCase();
        this.reason = reason;
        this.admin = admin;
        this.time = System.currentTimeMillis();
        this.endTime = 0;
        this.type = type;
        this.IP = IP;
    }

    EditBan(String victimUUID, String name, String reason, String admin, long endTime, int type) {
        this.id = 0;
        this.UUID = victimUUID;
        this.name = name.toLowerCase();
        this.reason = reason;
        this.admin = admin;
        this.time = System.currentTimeMillis();
        this.endTime = endTime;
        this.type = type;
        this.IP = null;
    }

    private EditBan() {
    }

    /*
     * Load from data line
     * as from this.toString()
     */
    public static EditBan loadBan(String data) {
        String[] d = data.split("\\|");
        return loadBan(d);
    }
    public static EditBan loadBan(String[] d) {
        if (d.length < 7) {
            return null;
        }
        EditBan e = new EditBan();
        e.UUID = d[0];
        e.name = d[1].toLowerCase();
        e.id = Integer.parseInt(d[2]);
        e.reason = d[3];
        e.admin = d[4];
        e.IP = (d[5].equals("null")) ? null : d[4];
        e.time = Long.parseLong(d[6]);
        e.endTime = Long.parseLong(d[7]);
        e.type = Integer.parseInt(d[8]);
        return e;
    }

    public String toString() {
        StringBuffer s = new StringBuffer(id);
        s.append(UUID);
        s.append("|");
        s.append(name);
        s.append("|");
        s.append(id);
        s.append("|");
        s.append(reason);
        s.append("|");
        s.append(admin);
        s.append("|");
        s.append(IP);
        s.append("|");
        s.append(time);
        s.append("|");
        s.append(endTime);
        s.append("|");
        s.append(type);
        return s.toString();
    }

    public boolean equals(Object object) {
        if (object instanceof String) {
            return ((String)object).toLowerCase().equals(this.UUID);
        } else if (object instanceof EditBan) {
            EditBan o = (EditBan) object;
            return o.UUID.equals(this.UUID) && o.admin.equals(this.admin) && o.name.equals(this.name) && o.reason.equals(this.reason)
                    && o.IP.equals(this.IP) && o.time == this.time && o.endTime == this.endTime && o.type == this.type;
        }
        return false;

    }
}
