package es.iessaladillo.pedrojoya.pr081.modelos;

import java.util.List;

// Class generated by Clever Models 

public class Lista {
    private Long dt;
    private Temp temp;
    private Float pressure;
    private Integer humidity;
    private List<Weather> weather;
    private Float speed;
    private Integer deg;
    private Float clouds;
    private Float rain;

    public Long getDt() {
        return dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public Float getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Float getSpeed() {
        return speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public Float getClouds() {
        return clouds;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public void setClouds(Float clouds) {
        this.clouds = clouds;
    }

    public void setRain(Float rain) {
        this.rain = rain;
    }
}