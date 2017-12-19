public static void main(String[]args){
        int[] values = new int[]{50, 70, 70, 42, 50, 80, 90, 90, 52, 22, 20, 52, 48, 42, 54, 20, 19, 24, 40, 82, 78, 52, 51, 62, 65};

        //---->Default graph using plotter<----
        LineGraph graph = new LineGraph(900,400);
        graph.setInterval(50);

        //Populating the graph
        for(int v : values) {
        graph.addValue(v);
        }

        Plotter.plot(graph);

        //---->Custom graph using plotter<----
        LineGraph customGraph = new LineGraph(1000,400);
        customGraph.setInterval(30);

        //Populating the graph
        for(int v : values) {
        customGraph.addValue(v*5);
        }

        customGraph.getGraphPath().setFill(Color.VIOLET);
        customGraph.getGraphPath().setStroke(Color.TRANSPARENT);

        customGraph.render(LineGraph.Render.GRAPH);
        Plotter.plot(customGraph);
        }