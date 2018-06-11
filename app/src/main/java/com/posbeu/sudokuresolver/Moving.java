package com.posbeu.sudokuresolver;

import java.util.List;

/**
 * Created by gposabella on 30/05/2016.
 */
public class Moving {

    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;
    private List<Moving> lista = null;
    private Chunk chunk;

    private int finx;
    private int finy;

    private int curx;
    private int cury;
    private boolean arrivato=false;

    public Moving(List<Moving> t, Chunk c, int x1, int y1, int x2, int y2) {
        chunk=c;
        curx = x1;
        cury = y1;
        finx = x2;
        finy = y2;

        startX = (x1<x2)?x1:x2;
        endX = (x1<x2)?x2:x1;
        startY= (y1<y2)?y1:y2;
        endY= (y1<y2)?y2:y1;


        this.lista = t;
    }



    public void nextStep() {
        if( arrivato)return;
        int delta = 2;
        int dx=-delta; int dy=0;

        int min = dist(finx, finy, curx - delta, cury);
        int d1 = dist(finx, finy, curx - delta, cury - delta);
        if( d1<min){dx=-delta;dy=-delta; min=d1;}
        d1 = dist(finx, finy, curx - delta, cury + delta);
        if( d1<min){dx=-delta;dy=delta; min=d1;}

        d1 = dist(finx, finy, curx + delta, cury);
        if( d1<min){dx=delta;dy=0; min=d1;}
        d1 = dist(finx, finy, curx + delta, cury - delta);
        if( d1<min){dx=delta;dy=-delta; min=d1;}
        d1 = dist(finx, finy, curx + delta, cury + delta);
        if( d1<min){dx=delta;dy=delta; min=d1;}
        d1 = dist(finx, finy, curx, cury - delta);
        if( d1<min){dx=0;dy=-delta; min=d1;}
        d1 = dist(finx, finy, curx, cury + delta);
        if( d1<min){dx=0;dy=delta; min=d1;}

        curx+=dx;
        cury+=dy;

        chunk.setX(curx);
        chunk.setY(cury);

        check(min);

    }

    public boolean isArrivato() {
        return arrivato;
    }

    private void check(int min) {
        if (min < 100 || (endX - startX < 5 && endY - startY < 5)) {
            arrivato=true;
            chunk.setX(finx);
            chunk.setY(finy);
            //   lista.remove(this);

        }
    }

    private int dist(int ax, int ay, int bx, int by) {
        return (ax - bx) * (ax - bx) + (ay - by) * (ay - by);
    }
}
