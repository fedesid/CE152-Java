package Assignment;

import java.awt.*;

public class PlotRandomMap extends Plot {
    Earth e;

    public PlotRandomMap(Double resolution){ //This method is used to print the random map.
        e = new Earth();
        e.generateMap(resolution);
    }

    @Override
    public void paintComponent(Graphics g){
        g.translate(-width/2, 0);

        for (Double[] coos: e.mapOfEarth.keySet()) {
            double altitude = e.mapOfEarth.get(coos);

            if (altitude > 3400){
                g.setColor(new Color(0xD4D3E8));
            }else if (altitude > 3200){
                g.setColor(new Color(0x341900));
            }else if (altitude > 2800){
                g.setColor(new Color(0x46260C));
            }else if (altitude > 2500){
                g.setColor(new Color(0x573316));
            }else if (altitude > 1500){
                g.setColor(new Color(0x532F0B));
            }else if (altitude > 1000){
                g.setColor(new Color(0x693B12));
            }else if (altitude > 700){
                g.setColor(new Color(0xAEA225));
            }else if (altitude > 400){
                g.setColor(new Color(0xEFDE1A));
            }else if (altitude > 200){
                g.setColor(new Color(0xF0EB1F));
            }else if (altitude > 0){
                g.setColor(new Color(0x2A5800));
            }else if (altitude > -100) {
                g.setColor(new Color(0xD328A7D6));
            }else if (altitude > -400){
                g.setColor(new Color(0x3D7AB5));
            }else if (altitude > -900){
                g.setColor(new Color(0x23589F));
            }else if (altitude > -1300){
                g.setColor(new Color(0x223FAA));
            }else if (altitude > -1800){
                g.setColor(new Color(0x1631A8));
            }else if (altitude > -2000){
                g.setColor(new Color(0x173162));
            }else if (altitude > -2500){
                g.setColor(new Color(0x132158));
            }else if (altitude > -4000){
                g.setColor(new Color(0x151E58));
            }else if (altitude <= -4000){
                g.setColor(new Color(0xF90A0058));
            }

            g.fillRect((int) Math.round(coos[0]), (int) Math.round(coos[1]), 2, 2);
        }
    }
}

