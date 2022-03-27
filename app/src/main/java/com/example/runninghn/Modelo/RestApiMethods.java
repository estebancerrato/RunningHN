package com.example.runninghn.Modelo;

public class RestApiMethods {
    private static final String ipaddress = "transportweb2.online";
    public static final String StringHttp = "http://";
    //EndPoint Urls
    private static final String GetEmple = "/APIexam/listacontactos.php";
    private static final String GetBuscar = "/API/validarLogin.php";
    private static final String getBuscarCorreo = "/API/listasingleusuario.php";

    private static final String setUpdate = "/APIexam/actualizarcontacto.php";
    private static final String CreateUsuario = "/API/crearusuario.php";
    private static final String listaPaises = "/API/listapaises.php";

    //metodo get
    public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointValidarLogin = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointBuscarCorreo = StringHttp + ipaddress + getBuscarCorreo;

    public static final String EndPointSetUpdateContact = StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
    public static final String EndPointListarPaises = StringHttp + ipaddress + listaPaises;
}


