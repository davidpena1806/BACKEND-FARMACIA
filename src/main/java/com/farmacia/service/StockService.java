package com.farmacia.service;

import com.farmacia.entity.LoteProducto;
import com.farmacia.entity.MovimientoInventario;
import com.farmacia.entity.Producto;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.LoteProductoRepository;
import com.farmacia.repository.MovimientoInventarioRepository;
import com.farmacia.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Único punto donde cambia el stock. Garantiza la regla de negocio central:
 * el stock nunca se edita a mano y cada cambio deja un MovimientoInventario (trazabilidad).
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductoRepository productoRepository;
    private final LoteProductoRepository loteRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    @Transactional
    public MovimientoInventario aumentar(Long productoId, Long loteId, int cantidad,
                                         Long usuarioId, String tipo, String referencia) {
        return mover(productoId, loteId, cantidad, usuarioId, tipo, referencia);
    }

    @Transactional
    public MovimientoInventario disminuir(Long productoId, Long loteId, int cantidad,
                                          Long usuarioId, String tipo, String referencia) {
        return mover(productoId, loteId, -cantidad, usuarioId, tipo, referencia);
    }

    private MovimientoInventario mover(Long productoId, Long loteId, int delta,
                                       Long usuarioId, String tipo, String referencia) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new BusinessException("El producto con Id " + productoId + " no existe."));

        int anterior = producto.getStock();
        int posterior = anterior + delta;
        if (posterior < 0) {
            throw new BusinessException("Stock insuficiente para '" + producto.getNombre()
                    + "'. Disponible: " + anterior + ", requerido: " + Math.abs(delta) + ".");
        }
        producto.setStock(posterior);
        productoRepository.save(producto);

        if (loteId != null) {
            LoteProducto lote = loteRepository.findById(loteId)
                    .orElseThrow(() -> new BusinessException("El lote con Id " + loteId + " no existe."));
            int stockLote = lote.getStock() + delta;
            if (stockLote < 0) {
                throw new BusinessException("Stock insuficiente en el lote '" + lote.getNumeroLote() + "'.");
            }
            lote.setStock(stockLote);
            loteRepository.save(lote);
        }

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProductoId(productoId);
        movimiento.setLoteProductoId(loteId);
        movimiento.setUsuarioId(usuarioId);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setCantidad(Math.abs(delta));
        movimiento.setStockAnterior(anterior);
        movimiento.setStockPosterior(posterior);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setReferencia(referencia);
        return movimientoRepository.save(movimiento);
    }
}
