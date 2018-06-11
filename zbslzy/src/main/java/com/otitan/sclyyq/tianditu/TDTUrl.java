package com.otitan.sclyyq.tianditu;

import java.util.Random;

/**
 * @author ��������: �ڻ� 
 * @E-mail yuh945@gmail.com
 * @version ����ʱ�䣺2012-10-13 ����05:22:27
 * ��˵��
 */
public class TDTUrl  {
    private TianDiTuTiledMapServiceType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;
    public TDTUrl(int level, int col, int row,TianDiTuTiledMapServiceType tiandituMapServiceType){
        this._level=level;
        this._col=col;
        this._row=row;
        this._tiandituMapServiceType=tiandituMapServiceType;
    }
    public String generatUrl(){
        /**
         * */
        StringBuilder url=new StringBuilder("http://t");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this._tiandituMapServiceType){
        case VEC_C:
             url.append(".tianditu.cn/vec_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX=").append(this._level).append("&TILECOL=").append(this._col).append("&TILEROW=").append(this._row);
             //url.append(".tianditu.com/vec_c/wmts?request=GetTile&service=wmts&version=1.0.0&layer=vec&style=default&format=tiles&TileMatrixSet=c&TILECOL=").append(this._col).append("&TILEROW=").append(this._row).append("&TILEMATRIX=").append(this._level);
             //url.append(".tianditu.com/DataServer?T=vec_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level); 
            break;
        case CVA_C:
        	 url.append(".tianditu.cn/cva_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX=").append(this._level).append("&TILECOL=").append(this._col).append("&TILEROW=").append(this._row);
        	 //url.append(".tianditu.com/DataServer?T=cva_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case CIA_C:
        	 url.append(".tianditu.cn/cia_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX=").append(this._level).append("&TILECOL=").append(this._col).append("&TILEROW=").append(this._row);
            break;
        case IMG_C:
        	 url.append(".tianditu.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX=").append(this._level).append("&TILECOL=").append(this._col).append("&TILEROW=").append(this._row);
            break;
            default:
                return null;
        }
        return url.toString();
    }
   
}
