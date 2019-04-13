import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class Data{
    String _item;
    int _value;
    public Data(String item, int value){
       _item = item;
       _value = value;
    }
}

interface Chart{
    public void display(ArrayList<Data> data);
}

class Spreadsheet implements Chart{
    public void display(ArrayList<Data> data){
        for(int i = 0 ; i < data.size() ; ++i){
            System.out.printf("%s %d%n", data.get(i)._item, data.get(i)._value);
        }
    }
}

class BarChart implements Chart{
    public void display(ArrayList<Data> data){
        for(int i = 0 ; i < data.size() ; ++i){
            for(int x = 0 ; x < data.get(i)._value ; ++x){
                System.out.print("=");
            }
            System.out.printf(" %s%n", data.get(i)._item);
        }
    }
}

class PieChart implements Chart{
    public void display(ArrayList<Data> data){
        int sum = 0;
        for(int i = 0 ; i < data.size() ; ++i) sum += data.get(i)._value;
        for(int i = 0 ; i < data.size() ; ++i){
            System.out.printf("%s %.1f%%%n", data.get(i)._item, (float)data.get(i)._value / sum * 100);
        }
    }
}

class Application{
    
    Application(){
        _data = new ArrayList<Data>();
        _chart = new ArrayList<Chart>();
    }
    
    public void addData(String item, int value){
        _data.add(new Data(item, value));
    }

    private int findItem(String item){
        int idx = -1;
        for(int i = 0 ; i < _data.size() ; ++i){
            if(_data.get(i)._item.equals(item)){
                idx = i;
                break;
            }
        }
        return idx;
    }

    public void changeData(String item, int value){
        int idx;
        if((idx = findItem(item)) != -1){
            _data.set(idx, new Data(item, value));
        }
        else{
            addData(item, value);
        }
    }

    public void registerChartMethod(Chart chart){
        _chart.add(chart);
    }

    public void display(){
        for(int i = 0 ; i < _chart.size() ; ++i){
            _chart.get(i).display(_data); 
        } 
    }

    private ArrayList<Data> _data;
    private ArrayList<Chart> _chart;
}

class Main{
    public static void main(String[] args){
        Application app = new Application();
        Scanner input = null;
        try{
            input = new Scanner(new FileReader(args[0]));
        }
        catch(IOException e){
            e.printStackTrace();
        }

        String command;
        while(input.hasNext()){
            command = input.next();
            switch(command){
            case "data":{
                String item = input.next();
                int value = input.nextInt();
                app.addData(item, value);
                break;
            }
            case "addChart":{
                String type = input.next();
                switch(type){
                    case "Spreadsheet": app.registerChartMethod(new Spreadsheet()); break;
                    case "BarChart": app.registerChartMethod(new BarChart()); break;
                    case "PieChart": app.registerChartMethod(new PieChart()); break;
                    default: break;
                }
                break;
            }
            case "change":{
                String chartType = input.next(), item = input.next();
                int value = input.nextInt();
                app.changeData(item, value);
                System.out.printf("%s change %s %d.%n", chartType, item, value);
                app.display();
                break;
            }
            default:
                break;
            }
        }
    }
}
