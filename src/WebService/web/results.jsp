<%@ page import="eu.smartsantander.androidExperimentation.ModelManager" %>
<%@ page import="eu.smartsantander.androidExperimentation.entities.Plugin" %>
<%@ page import="java.util.List" %>
<%@ page import="eu.smartsantander.androidExperimentation.entities.Result" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="eu.smartsantander.androidExperimentation.entities.Reading" %>
<%@ page import="java.util.Arrays" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AndroidExperimentation - SmartSantander</title>
    <script type="text/javascript">
        <%
            String expId = request.getParameter("id");
            Integer eId = null;
            try {
                eId = Integer.valueOf(expId);
            } catch (Exception e) {
                eId = null;
            }
            HashMap<String, Integer> sensors = new HashMap<String, Integer>();
            HashMap<String, Integer> devices = new HashMap<String, Integer>();

            //List<Result> results = ModelManager.getLastResults(eId);
            List<Result> results = ModelManager.getResults(eId);

            int counter=1;
            for (Result result : results) {
                if(result.getMessage()==null || result.getMessage().length()==0) continue;
                devices.put(String.valueOf(result.getDeviceId()),result.getDeviceId());
                try{
                Reading r = Reading.fromJson(result.getMessage());
                if (sensors.containsKey(r.getContext()))
                    sensors.put(r.getContext(), sensors.get(r.getContext()));
                else
                    sensors.put(r.getContext(), counter++);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }

      %>
        function openLiveChart()
        {
            var e = document.getElementById("device");
            var device = e.options[e.selectedIndex].value;
            var e2 = document.getElementById("sensor");
            var sensor = e2.options[e2.selectedIndex].value;
            var url='http://blanco.cti.gr:8080/chart.jsp?id='+ <%=expId%>+'&devId='+device+'&sensor='+sensor;
            window.open(url,'_blank');
        }


        function openMap()
        {
            /* var e = document.getElementById("device");
            var device = e.options[e.selectedIndex].value;*/
            var url='http://blanco.cti.gr:8080/map.jsp?id='+ <%=expId%>;
            window.open(url,'_blank');
        }

    </script>
</head>
<body>
<jsp:include page="./includes/header.html" flush="true"/>
<h3>Results for Experiment:<%=eId%>
</h3>

<h3>Draw Chart:</h3>

<form action="resultsChart.jsp">
    <input name=id value="<%=expId%>" hidden="true">
    Device Id:
    <select name="device" id="device">
        <%
            Object t[]=  devices.keySet().toArray();
            Arrays.sort(t);
            for (Object d : t  )
                out.print("<option value='" + d + "'>" + d + "</option>");
        %>
    </select>

    Sensor:
    <select name="sensor" id="sensor">
        <%
            for (String d : sensors.keySet())
                out.print("<option value='" + d + "'>" + d + "</option>");
        %>
    </select>
    <input type="submit" value="Draw">
    <input type="button" value="Live Chart" onclick="openLiveChart();"/>  Num. Values Required
    <input type="button" value="Map" onclick="openMap();"/>   Gps Required
</form>
<div class="datagrid">
    <table border="1">
        <thead>
        <tr>
            <th>Id</th>
            <th>Timestamp</th>
            <th>Reporting Device</th>
            <th>Message</th>

        </tr>
        </thead>
        <%

            for (Result result : results) {
                out.print("<tr>");
                out.print("<td>" + result.getId() + "</td>");
                if (result.getTimestamp()>0 ) {
                    out.print("<td>" + (new Date(result.getTimestamp())).toString() + "</td>");
                }else{
                    out.print("<td>-" + "</td>");
                }
                out.print("<td>" + result.getDeviceId() + "</td>");
                out.print("<td>" + result.getMessage() + "</td>");
                out.print("</tr>");
            }
        %>

    </table>
</div>
<jsp:include page="./includes/footer.html" flush="true"/>
</body>
</html>