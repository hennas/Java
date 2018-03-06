
import java.util.ArrayList;

public class Tekoalypelaaja implements Pelaaja {
    
    private int pun;
    private int sin;
    private int vih;
    private int kel;
    private int muuterikois;
    private int villit;
    private final String nimi;

    public Tekoalypelaaja() {
        this.nimi = "Nalle";
        this.pun = 0;
        this.sin = 0;
        this.vih = 0;
        this.kel = 0;
        this.muuterikois = 0;
        this.villit = 0;
    }
    
    @Override
    public int pelaa(ArrayList<Kortti> omatKortit, Kortti paallimmaisin, String vari, Pelitilanne tilanne) {  
        /*System.out.println("---");
        System.out.println("Pelaaja " + this.nimi);
        System.out.println("Päällimmäinen kortti: " + paallimmaisin);
        if (paallimmaisin.onVillikortti()) {
            System.out.println("\tValittu väri: " + vari);
        }
        System.out.println("");
        System.out.println("Kädessä:");*/
        this.pun = 0;
        this.sin = 0;
        this.vih = 0;
        this.kel = 0;
        this.muuterikois = 0;
        this.villit = 0;
        //Listataan, kuinka monta on mitäkin väriä ja korttityyppiä kädessä
        for (int i = 0; i < omatKortit.size(); i++) {
            //System.out.println("\t" + i + ": " + omatKortit.get(i));
            this.pun = omatKortit.get(i).getVari().equals("Punainen") ? this.pun + 1 : this.pun;
            this.sin = omatKortit.get(i).getVari().equals("Sininen") ? this.sin + 1 : this.sin;
            this.vih = omatKortit.get(i).getVari().equals("Vihreä") ? this.vih + 1 : this.vih;
            this.kel = omatKortit.get(i).getVari().equals("Keltainen") ? this.kel + 1 : this.kel;
            this.muuterikois = omatKortit.get(i).getKorttityyppi().equals("Ohitus") ? this.muuterikois + 1 : this.muuterikois;
            this.muuterikois = omatKortit.get(i).getKorttityyppi().equals("Suunnanvaihto") ? this.muuterikois + 1 : this.muuterikois;
            this.muuterikois = omatKortit.get(i).getKorttityyppi().equals("Nosta 2") ? this.muuterikois + 1 : this.muuterikois;
            this.villit = omatKortit.get(i).getVari().equals("-") ? this.villit + 1 : this.villit;
        }
        
        //Jos jollakin on alle 3 korttia, kannattaa pelata villit kortit pois niiden suuren pistearvon vuoksi
        if (jollainAlleKolme(tilanne) && this.villit != 0) {
            for (int i = 0; i < omatKortit.size(); i++) {
                if (omatKortit.get(i).getKorttityyppi().contains("Villi kortti")) {
                    return i;
                }
            }
        }
        
        //Jos pelaajia on kaksi, kannattaa pelata villi kortti + nosta 4 pois aina kun se on järkevää. Jos pelaajia on enemmän, saattaa pelata tilanteesta riippuen seuraavaa seuraavan hyödyksi.
        if (kannattaakoPelata(tilanne, "Villi kortti") && tilanne.getPelaajienPisteet().length == 2) {
            for (int i = 0; i < omatKortit.size(); i++) {
                if (omatKortit.get(i).getKorttityyppi().equals("Villi kortti + Nosta 4")) {
                    return i;
                }
            }
        }
        
        if (paallimmaisin.onVillikortti()) {
            //vain yhtä villi korttia kannattaa pitää varalla niiden suuren pistearvon takia
            if (this.villit < 2) {
                int kortinIndeksi = kortinIndeksiKunEdellaVilli(omatKortit, vari, tilanne);
                if (kortinIndeksi != -1) {
                    return kortinIndeksi;
                }
            }
            for (int k = 0; k < omatKortit.size(); k++) {
                if (omatKortit.get(k).getKorttityyppi().contains("Villi kortti")) {
                    return k;
                }
            }
        }
        
        if (paallimmaisin.onNosta() || paallimmaisin.onSuunnanvaihto() || paallimmaisin.onOhitus()) {
            if (this.villit < 2) {
                int kortinIndeksi = kortinIndeksiKunEdellaErikois(omatKortit, paallimmaisin.getVari(), tilanne, paallimmaisin.getKorttityyppi());
                if (kortinIndeksi != -1) {
                    return kortinIndeksi;
                }
            }
            for (int k = 0; k < omatKortit.size(); k++) {
                if (omatKortit.get(k).getKorttityyppi().contains("Villi kortti")) {
                    return k;
                }
            }
        }
        
        if (paallimmaisin.getKorttityyppi().equals("Numero")) {
            if (this.villit < 2) {
                int kortinIndeksi = kortinIndeksiKunEdellaNumero(omatKortit, paallimmaisin.getVari(), tilanne, paallimmaisin.getNumero());
                if (kortinIndeksi != -1) {
                    return kortinIndeksi;
                }
            }
            for (int k = 0; k < omatKortit.size(); k++) {
                if (omatKortit.get(k).getKorttityyppi().contains("Villi kortti")) {
                    return k;
                }
            }
        }
        
        // Valittu väri pysyy koko ajan muistissa, eikä tarkistuksissa oteta asiaa huomioon, joten seuraavaa kommentoitua koodinpätkää voi käyttää viimeisenä oljenkortena, jos edelliset tarkistukset eivät löydä sopivaa korttia...
        /*for (int i = 0; i < omatKortit.size(); i++) {
            if (omatKortit.get(i).getVari().equals(vari)) {
                return i;
            }
        }*/
        
        return -1;
    }

    @Override
    public String valitseVari(ArrayList<Kortti> omatKortit) {
        String palauta = enitenVaria();
        //Jos ei ole yhtään erikoiskorttia kannattaa palauttaa se väri, mitä on eniten. Jos on erikoiskortteja, kannattaa ensin tarkistaa, löytyykö niitä siinä värissä, jota on eniten. Jos ei, niin palautetaan ensimmäinen väri, josta löytyy erikoiskortti.
        if (this.muuterikois > 0) {
            for (int i = 0; i < omatKortit.size(); i++) {
                if (omatKortit.get(i).getKorttityyppi().equals("Ohitus") || omatKortit.get(i).getKorttityyppi().equals("Suunnanvaihto") || omatKortit.get(i).getKorttityyppi().equals("Nosta 2") && omatKortit.get(i).getVari().equals(palauta)) {
                    return omatKortit.get(i).getVari();
                }
            }
            for (int i = 0; i < omatKortit.size(); i++) {
                if (omatKortit.get(i).getKorttityyppi().equals("Ohitus") || omatKortit.get(i).getKorttityyppi().equals("Suunnanvaihto") || omatKortit.get(i).getKorttityyppi().equals("Nosta 2")) {
                    return omatKortit.get(i).getVari();
                }
            }
        }
        return palauta;
    }

    @Override
    public String nimi() {
        return this.nimi;
    }
    
    public String enitenVaria() {
        int eniten = this.pun;
        String palauta = "Punainen";
        if (this.sin > eniten) {
            eniten = this.sin;
            palauta = "Sininen";
        }
        if (this.vih > eniten) {
            eniten = this.vih;
            palauta = "Vihreä";
        }
        if (this.kel > eniten) {
            palauta = "Keltainen";
        }
        return palauta;
    }
    
    public boolean jollainAlleKolme(Pelitilanne tilanne) {
        int omaIndeksi = tilanne.getOmaIndeksi();
        int[] pelaajienKorttimaara = tilanne.getPelaajienKorttienLukumaarat();
        
        //Tarkistetaan, että ei ole ensimmäinen kierros (kaikkien korttien lukumäärät 0)
        for (int t = 0; t < pelaajienKorttimaara.length; t++) {
            if (pelaajienKorttimaara[t] == 0) {
                return false;
            }
        }
        
        for (int i = 0; i < pelaajienKorttimaara.length; i++) {
            if (pelaajienKorttimaara[i] < 3 && i != omaIndeksi) {
                return true;
            }
        }
        return false;
    }
    
    //Seuraavan metodin tarkistukset ovat ideaalisia kahden pelaajan pelissä. Useamman pelaajan pelissä saattaa tilanteesta riippuen pelata seuraavaa seuraavan hyödyksi. 
    public boolean kannattaakoPelata(Pelitilanne tilanne, String ktyyppi) {
        int omaIndeksi = tilanne.getOmaIndeksi();
        int[] pelaajienKorttimaara = tilanne.getPelaajienKorttienLukumaarat();
        int[] pelaajienpisteet = tilanne.getPelaajienPisteet();
        String suunta = tilanne.getSuunta();
        int seuraava;
        if (pelaajienpisteet.length > 1) {
            if (omaIndeksi == pelaajienKorttimaara.length - 1) {
                seuraava = suunta.equals("Myötäpäivään") ? 0 : omaIndeksi - 1;
            } else if (omaIndeksi == 0) {
                seuraava= suunta.equals("Myötäpäivään") ? omaIndeksi + 1 : pelaajienKorttimaara.length - 1;
            } else {
                seuraava = suunta.equals("Myötäpäivään") ? omaIndeksi + 1 : omaIndeksi - 1;
            }

            //Tämän hetkisessä versiossa seuraavat kohdat voisi yhdistää yhdeksi kohdaksi, mutta jatkokehitystä ajatellen pidän pätkän ennallaan. 
            if (ktyyppi.equals("Villi kortti")) {
                if (pelaajienKorttimaara[seuraava] <= pelaajienKorttimaara[omaIndeksi] || pelaajienpisteet[seuraava] >= pelaajienpisteet[omaIndeksi]) {
                    return true;
                }
                return false;
            }
            if (ktyyppi.equals("Nosta 2")) {
                if (pelaajienKorttimaara[seuraava] <= pelaajienKorttimaara[omaIndeksi] || pelaajienpisteet[seuraava] >= pelaajienpisteet[omaIndeksi]) {
                    return true;
                }
                return false;
            }
            if (ktyyppi.equals("Ohitus")) {
                if (pelaajienKorttimaara[seuraava] <= pelaajienKorttimaara[omaIndeksi] || pelaajienpisteet[seuraava] >= pelaajienpisteet[omaIndeksi]) {
                    return true;
                }
                return false;
            }
            if (ktyyppi.equals("Suunnanvaihto")) {
                if (pelaajienKorttimaara[seuraava] <= pelaajienKorttimaara[omaIndeksi] || pelaajienpisteet[seuraava] >= pelaajienpisteet[omaIndeksi]) {
                    return true;
                }
                return false;
            }  
        }
        return true;
    }
    
    public int suurimmanNumeronIndeksi(ArrayList<Integer> numerot) {
        int suurin = numerot.get(0);
        int suurimmanIndeksi = 0;
        for (int i = 0; i < numerot.size(); i++) {
            if (numerot.get(i) > suurin) {
                suurin = numerot.get(i);
                suurimmanIndeksi = i;
            }
        }
        return suurimmanIndeksi;
    }
    
    public int kortinIndeksiKunEdellaVilli(ArrayList<Kortti> kortit, String vari, Pelitilanne tilanne) {
        ArrayList<String> tyypit = new ArrayList<>();
        ArrayList<Integer> korttiennumerot = new ArrayList<>();
        ArrayList<Integer> korttienindeksit = new ArrayList<>();

        //Listataan jokaisen tiettyä väriä olevan kortin korttityyppi, numero ja indeksi 
        for (int i = 0; i < kortit.size(); i++) {
            if (kortit.get(i).getVari().equals(vari)) {
                tyypit.add(kortit.get(i).getKorttityyppi());
                korttiennumerot.add(kortit.get(i).getNumero());
                korttienindeksit.add(i);
            }
        }
        
        if (korttienindeksit.isEmpty()) {
            return -1;
        }
        
        //Listataan, kuinka monta on kutakin korttityyppiä. Tämän hetkisessä versiossa kohta on osittain turha, mutta pidän sen tällaisena mahdollista jatkokehitystä ajatellen.
        int ohitukset = 0;
        int suunnanvaihdot = 0;
        int nosta2 = 0;
        int numerot = 0;
        
        for (int i = 0; i < tyypit.size(); i++) {
            ohitukset = tyypit.get(i).equals("Ohitus") ? ohitukset + 1 : ohitukset;
            suunnanvaihdot = tyypit.get(i).equals("Suunnanvaihto") ? suunnanvaihdot + 1 : suunnanvaihdot;
            nosta2 = tyypit.get(i).equals("Nosta 2") ? nosta2 + 1 : nosta2;
            numerot = tyypit.get(i).equals("Numero") ? numerot + 1 : numerot;
        }
        
        int suurimmanIndeksi = suurimmanNumeronIndeksi(korttiennumerot);
        
        if (ohitukset == 0 && suunnanvaihdot == 0 && nosta2 == 0) {
            return korttienindeksit.get(suurimmanIndeksi);
        } else {
            if (kannattaakoPelata(tilanne, "Nosta 2")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Nosta 2")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Ohitus")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Ohitus")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Suunnanvaihto")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Suunnanvaihto")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            
            int random = (int) (Math.random() * tyypit.size());
            
            if (korttiennumerot.get(random) == 0 && tyypit.size() > 1) {
                if (random == tyypit.size() - 1) {
                    return korttienindeksit.get(random - 1);
                } else {
                    return korttienindeksit.get(random + 1);
                }
            }
            
            return korttienindeksit.get(random);
        }
    }
    
    public int kortinIndeksiKunEdellaErikois(ArrayList<Kortti> kortit, String vari, Pelitilanne tilanne, String paallimmaisentyyppi) {
        ArrayList<String> tyypit = new ArrayList<>();
        ArrayList<Integer> korttiennumerot = new ArrayList<>();
        ArrayList<Integer> korttienindeksit = new ArrayList<>();
        ArrayList<String> erivariSamatyyppi = new ArrayList<>();
        ArrayList<Integer> korttienindeksitErivarit = new ArrayList<>();

        for (int i = 0; i < kortit.size(); i++) {
            //Listataan samaa tyyppiä olevien korttien värit ja indeksit
            if (kortit.get(i).getKorttityyppi().equals(paallimmaisentyyppi) && !kortit.get(i).getVari().equals(vari)) {
                erivariSamatyyppi.add(kortit.get(i).getVari());
                korttienindeksitErivarit.add(i);
            }
            //Listataan samaa väriä olevien korttien korttityypit, numerot ja indeksit
            if (kortit.get(i).getVari().equals(vari)) {
                tyypit.add(kortit.get(i).getKorttityyppi());
                korttiennumerot.add(kortit.get(i).getNumero());
                korttienindeksit.add(i);
            }
        }
        
        if (korttienindeksitErivarit.isEmpty() && tyypit.isEmpty()) {
            return -1;
        }
        
        String enitenvaria = enitenVaria();
        
        if (!korttienindeksitErivarit.isEmpty() && tyypit.isEmpty()) {
            for (int i = 0; i < erivariSamatyyppi.size(); i++) {
                if (erivariSamatyyppi.get(i).equals(enitenvaria)) {
                    return korttienindeksitErivarit.get(i);
                }
            }
            return korttienindeksitErivarit.get(0);
        }
        
        int ohitukset = 0;
        int suunnanvaihdot = 0;
        int nosta2 = 0;
        int numerot = 0;
        
        for (int i = 0; i < tyypit.size(); i++) {
            ohitukset = tyypit.get(i).equals("Ohitus") ? ohitukset + 1 : ohitukset;
            suunnanvaihdot = tyypit.get(i).equals("Suunnanvaihto") ? suunnanvaihdot + 1 : suunnanvaihdot;
            nosta2 = tyypit.get(i).equals("Nosta 2") ? nosta2 + 1 : nosta2;
            numerot = tyypit.get(i).equals("Numero") ? numerot + 1 : numerot;
        }
        
        int suurimmanIndeksi = suurimmanNumeronIndeksi(korttiennumerot);
        
        if (ohitukset == 0 && suunnanvaihdot == 0 && nosta2 == 0) {
            //Jos ei ole erikoiskortteja samassa värissä, kannattaa käyttää samantyyppinen erikoiskortti, jonka vaihtaa värin siihen väriin, jota on eniten.
            for (int i = 0; i < erivariSamatyyppi.size(); i++) {
                if (erivariSamatyyppi.get(i).equals(enitenvaria)) {
                    return korttienindeksitErivarit.get(i);
                }
            }
            //Jos ei ole erikoiskortteja samassa värissä ja jollain pelaajalla on jo alle 3 korttia, kannattaa hankkiutua eroon erivärisistä erikoiskorteista suuremman pistearvon vuoksi.
            if (jollainAlleKolme(tilanne) && !korttienindeksitErivarit.isEmpty()) {
                return korttienindeksitErivarit.get(0);
            }
            return korttienindeksit.get(suurimmanIndeksi);
        } else {
            if (kannattaakoPelata(tilanne, "Nosta 2")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Nosta 2")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Ohitus")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Ohitus")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Suunnanvaihto")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Suunnanvaihto")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            
            int random = (int) (Math.random() * tyypit.size());
            
            if (korttiennumerot.get(random) == 0 && tyypit.size() > 1) {
                if (random == tyypit.size() - 1) {
                    return korttienindeksit.get(random - 1);
                } else {
                    return korttienindeksit.get(random + 1);
                }
            }

            return korttienindeksit.get(random);
        }
    }
    
    public int kortinIndeksiKunEdellaNumero(ArrayList<Kortti> kortit, String vari, Pelitilanne tilanne, int paallimmaisenNumero) {
        ArrayList<String> tyypit = new ArrayList<>();
        ArrayList<Integer> korttiennumerot = new ArrayList<>();
        ArrayList<Integer> korttienindeksit = new ArrayList<>();
        ArrayList<String> korttienvarit = new ArrayList<>();
        ArrayList<Integer> korttienindeksitErivarit = new ArrayList<>();

        for (int i = 0; i < kortit.size(); i++) {
            //Listataan samaa numeroa olevien korttien värit ja indeksit
            if (kortit.get(i).getNumero() == paallimmaisenNumero && !kortit.get(i).getVari().equals(vari)) {
                korttienvarit.add(kortit.get(i).getVari());
                korttienindeksitErivarit.add(i);
            }
            //Listataan samaa väriä olevien korttien korttityypit, numerot ja indeksit
            if (kortit.get(i).getVari().equals(vari)) {
                tyypit.add(kortit.get(i).getKorttityyppi());
                korttiennumerot.add(kortit.get(i).getNumero());
                korttienindeksit.add(i);
            }
        }
        
        if (korttienindeksitErivarit.isEmpty() && tyypit.isEmpty()) {
            return -1;
        }
        
        String enitenvaria = enitenVaria();
        
        if (!korttienindeksitErivarit.isEmpty() && tyypit.isEmpty()) {
            for (int i = 0; i < korttienvarit.size(); i++) {
                if (korttienvarit.get(i).equals(enitenvaria)) {
                    return korttienindeksitErivarit.get(i);
                }
            }
            return korttienindeksitErivarit.get(0);
        }
        
        int ohitukset = 0;
        int suunnanvaihdot = 0;
        int nosta2 = 0;
        int numerot = 0;
        
        for (int i = 0; i < tyypit.size(); i++) {
            ohitukset = tyypit.get(i).equals("Ohitus") ? ohitukset + 1 : ohitukset;
            suunnanvaihdot = tyypit.get(i).equals("Suunnanvaihto") ? suunnanvaihdot + 1 : suunnanvaihdot;
            nosta2 = tyypit.get(i).equals("Nosta 2") ? nosta2 + 1 : nosta2;
            numerot = tyypit.get(i).equals("Numero") ? numerot + 1 : numerot;
        }
        
        int suurimmanIndeksi = suurimmanNumeronIndeksi(korttiennumerot);
        
        if (ohitukset == 0 && suunnanvaihdot == 0 && nosta2 == 0) {
            //Jos ei ole erikoiskortteja samassa värissä, kannattaa vaihtaa samannumeroisella kortilla väri siksi väriksi, jota on eniten.
            for (int i = 0; i < korttienvarit.size(); i++) {
                if (korttienvarit.get(i).equals(enitenvaria)) {
                    return korttienindeksitErivarit.get(i);
                }
            }
            return korttienindeksit.get(suurimmanIndeksi);
        } else {
            if (kannattaakoPelata(tilanne, "Nosta 2")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Nosta 2")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Ohitus")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Ohitus")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            if (kannattaakoPelata(tilanne, "Suunnanvaihto")) {
                for (int i = 0; i < tyypit.size(); i++) {
                    if (tyypit.get(i).equals("Suunnanvaihto")) {
                        return korttienindeksit.get(i);
                    }
                }
            }
            int random = (int) (Math.random() * tyypit.size());
            
            if (korttiennumerot.get(random) == 0 && tyypit.size() > 1) {
                if (random == tyypit.size() - 1) {
                    return korttienindeksit.get(random - 1);
                } else {
                    return korttienindeksit.get(random + 1);
                }
            }

            return korttienindeksit.get(random);
        }
    }
}