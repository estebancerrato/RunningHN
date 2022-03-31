package com.example.runninghn.Modelo;

public class RestApiMethods {
    private static final String ipaddress = "transportweb2.online";
    public static final String StringHttp = "http://";
    //EndPoint Urls
   // private static final String GetEmple = "/APIexam/listacontactos.php";
    private static final String GetBuscar = "/API/validarLogin.php";
    private static final String getBuscarCorreo = "/API/listasingleusuario.php";
    private static final String setUpdate = "/API/actualizarusuario.php";
    private static final String CreateUsuario = "/API/crearusuario.php";
    private static final String listaPaises = "/API/listapaises.php";
    private static final String listaUsuarioPaise = "/API/listasingleusuariopais.php";


    //metodo get
    //public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointValidarLogin = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointBuscarCorreo = StringHttp + ipaddress + getBuscarCorreo;

    public static final String EndPointSetUpdateUser= StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
    public static final String EndPointListarPaises = StringHttp + ipaddress + listaPaises;
    public static final String EndPointListarUsuarioPaise = StringHttp + ipaddress + listaUsuarioPaise;
    public static String EndPointBuscarUsuario;

    String prueba = "http://transportweb2.online/API/listasingleusuariopais.php";
}


