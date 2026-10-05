suppressPackageStartupMessages(library(tcltk))

SimularInventarioSQ <- function(d_media, d_sd, lead_time, s_reorden, q_pedido, dias_sim) {
  inventario_actual <- q_pedido
  orden_en_transito <- FALSE
  dias_para_llegada <- 0
  
  costo_ordenar <- 50
  costo_mantener_dia <- 0.5
  costo_faltante_unidad <- 5
  
  inv_historia <- numeric(dias_sim)
  costo_total_mantenimiento <- 0
  costo_total_ordenar <- 0
  costo_total_faltante <- 0
  ordenes_realizadas <- 0
  
  set.seed(123)
  
  for (dia in 1:dias_sim) {
    if (orden_en_transito) {
      dias_para_llegada <- dias_para_llegada - 1
      if (dias_para_llegada <= 0) {
        inventario_actual <- inventario_actual + q_pedido
        orden_en_transito <- FALSE
      }
    }
    
    demanda_dia <- max(0, round(rnorm(1, mean = d_media, sd = d_sd)))
    
    if (inventario_actual >= demanda_dia) {
      inventario_actual <- inventario_actual - demanda_dia
    } else {
      unidades_faltantes <- demanda_dia - inventario_actual
      costo_total_faltante <- costo_total_faltante + (unidades_faltantes * costo_faltante_unidad)
      inventario_actual <- 0
    }
    
    if (inventario_actual <= s_reorden && !orden_en_transito) {
      orden_en_transito <- TRUE
      dias_para_llegada <- lead_time
      costo_total_ordenar <- costo_total_ordenar + costo_ordenar
      ordenes_realizadas <- ordenes_realizadas + 1
    }
    
    costo_total_mantenimiento <- costo_total_mantenimiento + (inventario_actual * costo_mantener_dia)
    inv_historia[dia] <- inventario_actual
  }
  
  costo_total <- costo_total_mantenimiento + costo_total_ordenar + costo_total_faltante
  inv_promedio <- mean(inv_historia)
  
  return(list(
    "Inventario Promedio" = sprintf("%.2f unidades", inv_promedio),
    "Ordenes Realizadas" = sprintf("%d pedidos", ordenes_realizadas),
    "Costo Mantenimiento" = sprintf("$%.2f", costo_total_mantenimiento),
    "Costo de Pedido" = sprintf("$%.2f", costo_total_ordenar),
    "Costo por Faltantes" = sprintf("$%.2f", costo_total_faltante),
    "Costo Total Operativo" = sprintf("$%.2f", costo_total)
  ))
}

crear_interfaz_inventarios <- function() {
  tt <- tktoplevel()
  tkwm.title(tt, "Tema 4.3.2 - Sistemas de Inventarios (s, Q) en R")
  tkwm.geometry(tt, "500x480")
  tkwm.resizable(tt, FALSE, FALSE)
  
  var_d_media   <- tclVar("10")
  var_d_sd      <- tclVar("2")
  var_lead_time <- tclVar("3")
  var_s         <- tclVar("15")
  var_q         <- tclVar("50")
  var_dias      <- tclVar("30")
  
  frame_inputs <- tkwidget(tt, "labelframe", text = " Parametros de Inventario (s, Q) ", padx = 10, pady = 10)
  tkpack(frame_inputs, fill = "x", padx = 15, pady = 10)
  
  lbls <- c("Demanda Media Diaria:", "Desv. Est. Demanda:", "Tiempo de Entrega (dias):", 
            "Punto de Reorden (s):", "Lote de Pedido (Q):", "Dias a Simular:")
  vars <- list(var_d_media, var_d_sd, var_lead_time, var_s, var_q, var_dias)
  
  for (i in 1:length(lbls)) {
    lbl <- tklabel(frame_inputs, text = lbls[i])
    ent <- tkentry(frame_inputs, textvariable = vars[[i]], width = 10)
    row_idx <- (i - 1) %/% 2
    col_idx <- ((i - 1) %% 2) * 2
    tkgrid(lbl, row = row_idx, column = col_idx, sticky = "w", padx = 5, pady = 3)
    tkgrid(ent, row = row_idx, column = col_idx + 1, padx = 5, pady = 3)
  }
  
  frame_results <- tkwidget(tt, "labelframe", text = " Resultados de la Simulacion ", padx = 10, pady = 10)
  tkpack(frame_results, fill = "both", expand = TRUE, padx = 15, pady = 10)
  
  listbox_results <- tklistbox(frame_results, height = 7)
  tkpack(listbox_results, fill = "both", expand = TRUE, padx = 5, pady = 5)
  
  ejecutar_simulacion <- function() {
    d_m  <- as.numeric(tclvalue(var_d_media))
    d_s  <- as.numeric(tclvalue(var_d_sd))
    lt   <- as.numeric(tclvalue(var_lead_time))
    s_val<- as.numeric(tclvalue(var_s))
    q_val<- as.numeric(tclvalue(var_q))
    dias <- as.numeric(tclvalue(var_dias))
    
    tkdelete(listbox_results, 0, "end")
    
    if (any(is.na(c(d_m, d_s, lt, s_val, q_val, dias))) || min(c(d_m, d_s, lt, s_val, q_val, dias)) < 0) {
      tkmessagebox(type = "ok", icon = "error", message = "Ingresa valores numericos validos.")
      return()
    }
    
    tryCatch({
      res <- SimularInventarioSQ(d_m, d_s, lt, s_val, q_val, dias)
      for (nombre in names(res)) {
        linea <- paste0(nombre, ": ", res[[nombre]])
        tkinsert(listbox_results, "end", linea)
      }
    }, error = function(e) {
      tkmessagebox(type = "ok", icon = "error", message = e$message)
    })
  }
  
  btn_calcular <- tkbutton(tt, text = "Ejecutar Simulacion", command = ejecutar_simulacion)
  tkpack(btn_calcular, pady = 5)
  
  tcl("update")
  return(tt)
}

ventana <- crear_interfaz_inventarios()