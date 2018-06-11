package com.otitan.sclyyq.tianditu;

import java.util.Random;

/**
 * @author ��������: �ڻ� 
 * @E-mail yuh945@gmail.com
 * @version ����ʱ�䣺2012-10-13 ����05:22:27
 * ��˵��
 */
public class BTDTUrl  {
    private TianDiTuTiledMapServiceType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;
    public BTDTUrl(int level, int col, int row,TianDiTuTiledMapServiceType tiandituMapServiceType){
        this._level=level;
        this._col=col;
        this._row=row;
        this._tiandituMapServiceType=tiandituMapServiceType;
    }
    public String generatUrl(){
        /**
         * ���ͼʸ����Ӱ��
         * �ٶ�ʸ��http://online3.map.bdimg.com/onlinelabel/?qt=tile&x=6382&y=1817&z=15&styles=pl
         * */
        StringBuilder url=new StringBuilder("http://");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this._tiandituMapServiceType){
        case VEC_C:
             url.append("online3.map.bdimg.com/onlinelabel/?qt=tile&x=").append(this._col).append("&y=").append(this._row).append("&z=").append(this._level).append("&styles=pl");
             //url.append(".tianditu.com/vec_c/wmts?request=GetTile&service=wmts&version=1.0.0&layer=vec&style=default&format=tiles&TileMatrixSet=c&TILECOL=").append(this._col).append("&TILEROW=").append(this._row).append("&TILEMATRIX=").append(this._level);
             //url.append(".tianditu.com/DataServer?T=vec_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level); 
            break;
        case CVA_C:
        	 url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
        	 //url.append(".tianditu.com/DataServer?T=cva_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case CIA_C:
        	 url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case IMG_C:
        	 url.append(".tianditu.com/DataServer?T=img_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
            default:
                return null;
        }
        return url.toString();
    }
   
}
