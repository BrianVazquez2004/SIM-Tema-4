suppressPackageStartupMessages(library(tcltk))

ModeloLineaEspera <- function(lambda, mu) {
  if (lambda >= mu) {
    stop("La tasa de llegada (lambda) debe ser menor que la tasa de servicio (mu).")
  }
  
  rho <- lambda / mu
  lq  <- (lambda^2) / (mu * (mu - lambda))
  l   <- lambda / (mu - lambda)
  wq  <- lambda / (mu * (mu - lambda))
  w   <- 1 / (mu - lambda)
  
  return(list(
    "Factor de Utilizacion (rho)" = sprintf("%.2f%%", rho * 100),
    "Longitud Promedio Cola (Lq)" = sprintf("%.4f clientes", lq),
    "Longitud Promedio Sistema (L)" = sprintf("%.4f clientes", l),
    "Tiempo Promedio Cola (Wq)" = sprintf("%.4f horas", wq),
    "Tiempo Promedio Sistema (W)" = sprintf("%.4f horas", w)
  ))
}

crear_interfaz_linea_espera <- function() {
  tt <- tktoplevel()
  tkwm.title(tt, "Tema 4.3.1 - Lineas de Espera (M/M/1) en R")
  tkwm.geometry(tt, "480x400")
  tkwm.resizable(tt, FALSE, FALSE)
  
  var_lambda <- tclVar("3.0")
  var_mu     <- tclVar("5.0")
  
  frame_inputs <- tkwidget(tt, "labelframe", text = " Parametros del Sistema (M/M/1) ", padx = 10, pady = 10)
  tkpack(frame_inputs, fill = "x", padx = 15, pady = 10)
  
  lbl_lambda <- tklabel(frame_inputs, text = "Tasa de Llegada (lambda):")
  ent_lambda <- tkentry(frame_inputs, textvariable = var_lambda, width = 12)
  tkgrid(lbl_lambda, row = 0, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_lambda, row = 0, column = 1, padx = 5, pady = 5)
  
  lbl_mu <- tklabel(frame_inputs, text = "Tasa de Servicio (mu):")
  ent_mu <- tkentry(frame_inputs, textvariable = var_mu, width = 12)
  tkgrid(lbl_mu, row = 1, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_mu, row = 1, column = 1, padx = 5, pady = 5)
  
  frame_results <- tkwidget(tt, "labelframe", text = " Resultados ", padx = 10, pady = 10)
  tkpack(frame_results, fill = "both", expand = TRUE, padx = 15, pady = 10)
  
  listbox_results <- tklistbox(frame_results, height = 6)
  tkpack(listbox_results, fill = "both", expand = TRUE, padx = 5, pady = 5)
  
  ejecutar_calculo <- function() {
    lam_val <- as.numeric(tclvalue(var_lambda))
    mu_val  <- as.numeric(tclvalue(var_mu))
    
    tkdelete(listbox_results, 0, "end")
    
    if (is.na(lam_val) || is.na(mu_val) || lam_val <= 0 || mu_val <= 0) {
      tkmessagebox(type = "ok", icon = "error", message = "Valores invalidos.")
      return()
    }
    
    tryCatch({
      res <- ModeloLineaEspera(lam_val, mu_val)
      for (nombre in names(res)) {
        linea <- paste0(nombre, ": ", res[[nombre]])
        tkinsert(listbox_results, "end", linea)
      }
    }, error = function(e) {
      tkmessagebox(type = "ok", icon = "error", message = e$message)
    })
  }
  
  btn_calcular <- tkbutton(tt, text = "Calcular Rendimiento", command = ejecutar_calculo)
  tkpack(btn_calcular, pady = 5)
  
  tcl("update")
  return(tt)
}

ventana <- crear_interfaz_linea_espera()