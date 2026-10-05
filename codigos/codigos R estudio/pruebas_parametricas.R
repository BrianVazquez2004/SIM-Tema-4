suppressPackageStartupMessages(library(tcltk))

PruebaTStudent <- function(datos_vec, mu_hipotesis, alfa) {
  n <- length(datos_vec)
  media_obs <- mean(datos_vec)
  sd_obs <- sd(datos_vec)
  
  if (sd_obs == 0) {
    stop("La desviacion estandar de los datos es 0.")
  }
  
  t_stat <- (media_obs - mu_hipotesis) / (sd_obs / sqrt(n))
  df <- n - 1
  p_valor <- 2 * pt(-abs(t_stat), df = df)
  
  decision <- if (p_valor < alfa) {
    "Se rechaza H0 (Diferencia significativa)"
  } else {
    "No se rechaza H0 (Sin diferencia significativa)"
  }
  
  return(list(
    "Muestra (n)" = sprintf("%d observaciones", n),
    "Media Muestral" = sprintf("%.4f", media_obs),
    "Desviacion Estandar" = sprintf("%.4f", sd_obs),
    "Estadistico t" = sprintf("%.4f", t_stat),
    "Grados de Libertad" = sprintf("%d", df),
    "p-valor" = sprintf("%.5f", p_valor),
    "Resultado" = decision
  ))
}

crear_interfaz_pruebas <- function() {
  tt <- tktoplevel()
  tkwm.title(tt, "Tema 4.3.3 - Pruebas Parametricas (Prueba t) en R")
  tkwm.geometry(tt, "500x460")
  tkwm.resizable(tt, FALSE, FALSE)
  
  var_datos <- tclVar("10.2, 9.8, 10.5, 10.1, 9.9, 10.3, 10.0, 9.7")
  var_mu0   <- tclVar("10.0")
  var_alfa  <- tclVar("0.05")
  
  frame_inputs <- tkwidget(tt, "labelframe", text = " Parametros de la Prueba t (1 Muestra) ", padx = 10, pady = 10)
  tkpack(frame_inputs, fill = "x", padx = 15, pady = 10)
  
  lbl_datos <- tklabel(frame_inputs, text = "Datos (separados por coma):")
  ent_datos <- tkentry(frame_inputs, textvariable = var_datos, width = 35)
  tkgrid(lbl_datos, row = 0, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_datos, row = 0, column = 1, padx = 5, pady = 5)
  
  lbl_mu0 <- tklabel(frame_inputs, text = "Media Hipotetizada (mu0):")
  ent_mu0 <- tkentry(frame_inputs, textvariable = var_mu0, width = 12)
  tkgrid(lbl_mu0, row = 1, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_mu0, row = 1, column = 1, sticky = "w", padx = 5, pady = 5)
  
  lbl_alfa <- tklabel(frame_inputs, text = "Nivel de Significancia (alfa):")
  ent_alfa <- tkentry(frame_inputs, textvariable = var_alfa, width = 12)
  tkgrid(lbl_alfa, row = 2, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_alfa, row = 2, column = 1, sticky = "w", padx = 5, pady = 5)
  
  frame_results <- tkwidget(tt, "labelframe", text = " Resultado Estadistico ", padx = 10, pady = 10)
  tkpack(frame_results, fill = "both", expand = TRUE, padx = 15, pady = 10)
  
  listbox_results <- tklistbox(frame_results, height = 7)
  tkpack(listbox_results, fill = "both", expand = TRUE, padx = 5, pady = 5)
  
  ejecutar_prueba <- function() {
    cadena_datos <- tclvalue(var_datos)
    mu0_val      <- as.numeric(tclvalue(var_mu0))
    alfa_val     <- as.numeric(tclvalue(var_alfa))
    
    tkdelete(listbox_results, 0, "end")
    
    vec_datos <- as.numeric(unlist(strsplit(cadena_datos, ",")))
    vec_datos <- vec_datos[!is.na(vec_datos)]
    
    if (length(vec_datos) < 2 || is.na(mu0_val) || is.na(alfa_val) || alfa_val <= 0 || alfa_val >= 1) {
      tkmessagebox(type = "ok", icon = "error", message = "Ingresa al menos 2 datos numericos y un alfa entre 0 y 1.")
      return()
    }
    
    tryCatch({
      res <- PruebaTStudent(vec_datos, mu0_val, alfa_val)
      for (nombre in names(res)) {
        linea <- paste0(nombre, ": ", res[[nombre]])
        tkinsert(listbox_results, "end", linea)
      }
    }, error = function(e) {
      tkmessagebox(type = "ok", icon = "error", message = e$message)
    })
  }
  
  btn_calcular <- tkbutton(tt, text = "Ejecutar Prueba t", command = ejecutar_prueba)
  tkpack(btn_calcular, pady = 5)
  
  tcl("update")
  return(tt)
}

ventana <- crear_interfaz_pruebas()