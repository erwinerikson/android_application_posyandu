package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.HistoryPub
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.model.WebResponse

interface AllRepo {

    fun getCheckHistory(kode: String, id: String, onResult: (ArrayList<CheckHistory>) -> Unit, onError: (String) -> Unit)
    fun getMedicationHistory(kode: String, id: String, onResult: (ArrayList<MedicationHistory>) -> Unit, onError: (String) -> Unit)
    fun getHistoryPub(user: String, onResult: (ArrayList<HistoryPub>) -> Unit, onError: (String) -> Unit)
    fun getSchedulePub(onResult: (ArrayList<SchedulePub>) -> Unit, onError: (String) -> Unit)
    fun getDataImmunization(kode: String, onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit)
    fun getAllDataImmunization(onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit)
    fun getDataCheck(onResult: (ArrayList<DataCheck>) -> Unit, onError: (String) -> Unit)
    fun getDataMedication(onResult: (ArrayList<DataMedication>) -> Unit, onError: (String) -> Unit)
    fun getDataMidwife(onResult: (ArrayList<DataMidwife>) -> Unit, onError: (String) -> Unit)
    fun saveImmunization(idUser: String, idBayi: String, idImun: String, berat: String, tinggi: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveImmunPregnant(idUser: String, idIbu: String, idImun: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveCheck(idUser: String, idPasien: String, idPeriksa: String, berat: String, tinggi: String, tensi: String, hasil: String, idBidan: String, harga: String, kode: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveMedication(idUser: String, idPasien: String, idObat: String, qty: String, kode: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveFinance(kdTrans: String, kdSumber: String, desc: String, sumber: String, nominal: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveDataCheck(nama: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveDataMidwife(nama: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveDataImmunization(nama: String, harga: String, kode: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveDataMedication(nama: String, satuan: String, harga: String, stok: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun editSchedule(tgl: String, ket: String, status: String, id: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun saveSchedule(tgl: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
}