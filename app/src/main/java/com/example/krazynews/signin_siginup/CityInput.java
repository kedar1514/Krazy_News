package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krazynews.MainActivity;
import com.example.krazynews.R;

import java.util.ArrayList;

//  <<,
public class CityInput extends AppCompatActivity {
    private Button next;
    private EditText editText;
    private LinearLayout pune,mumbai,delhi, chennai,lucknow,jaipur,bangalore,agra,chandigarh, long_text;
    private TextView pune_text,mumbai_text,delhi_text, chennai_text,lucknow_text,jaipur_text,bangalore_text,agra_text,chandigarh_text, empty_city;
    private TextView bold_text;
    private GridLayout main_image;
    private String name, selectedCity;
    private Spinner spinner;
    private TextView spinnerText;
    private ArrayList<String> cityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_input);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        //transition elemnts
        next = findViewById(R.id.next);
        bold_text = findViewById(R.id.bold);
        long_text = findViewById(R.id.longText);
        main_image = findViewById(R.id.category_block);

        //spinner initilization
        spinner = findViewById(R.id.searchable_spinner);
//        spinnerText = findViewById(R.id.spinner_text);
        cityList  = new ArrayList<>();
        cityList.add("Select City");
        addCity();
        spinner.setAdapter(new ArrayAdapter<>(CityInput.this,
                android.R.layout.simple_spinner_dropdown_item,cityList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    selectedCity = "Please Select City";
                }else{
                    String city = parent.getItemAtPosition(position).toString();
                    selectedCity = city;
                }
                if(position!=109 &&position!=2 &&position!=49 &&position!=245 &&position!=355 &&position!=113 &&position!=145 &&position!=392 &&position!=465){
                    pune.setBackgroundResource(R.drawable.city_background);
                    pune_text.setTextColor(0xFFFD676C);

                    mumbai.setBackgroundResource(R.drawable.city_background);
                    mumbai_text.setTextColor(0xFFFD676C);

                    delhi.setBackgroundResource(R.drawable.city_background);
                    delhi_text.setTextColor(0xFFFD676C);

                    chennai.setBackgroundResource(R.drawable.city_background);
                    chennai_text.setTextColor(0xFFFD676C);

                    lucknow.setBackgroundResource(R.drawable.city_background);
                    lucknow_text.setTextColor(0xFFFD676C);

                    jaipur.setBackgroundResource(R.drawable.city_background);
                    jaipur_text.setTextColor(0xFFFD676C);

                    bangalore.setBackgroundResource(R.drawable.city_background);
                    bangalore_text.setTextColor(0xFFFD676C);

                    agra.setBackgroundResource(R.drawable.city_background);
                    agra_text.setTextColor(0xFFFD676C);

                    chandigarh.setBackgroundResource(R.drawable.city_background);
                    chandigarh_text.setTextColor(0xFFFD676C);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCity.equals("Please Select City"))
                {
                    empty_city.setText("Please choose your city.");
                }
                else{
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        Pair[] pairs = new Pair[5];
                        pairs[0] = new Pair<View, String>(bold_text,"bold");
                        pairs[1] = new Pair<View, String>(next,"fixed_button");
                        pairs[2] = new Pair<View, String>(spinner,"edit_text");
                        pairs[3] = new Pair<View, String>(long_text,"long_text");
                        pairs[4] = new Pair<View, String>(main_image,"main_image");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CityInput.this,pairs);
                        Intent i = new Intent(CityInput.this, GetEmail.class);
                        i.putExtra("name",name);
                        i.putExtra("city",selectedCity);
                        startActivity(i,options.toBundle());
                        finish();
                    }
                    else{
                        startActivity(new Intent(CityInput.this, GetEmail.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                }
            }
        });

        pune = findViewById(R.id.pune);
        mumbai = findViewById(R.id.mumbai);
        delhi = findViewById(R.id.delhi);
        chennai = findViewById(R.id.chennai);
        lucknow = findViewById(R.id.lucknow);
        jaipur = findViewById(R.id.jaipur);
        bangalore = findViewById(R.id.banglore);
        agra = findViewById(R.id.agra);
        chandigarh = findViewById(R.id.chandigarh);

        empty_city =findViewById(R.id.empty_city);
        pune_text = findViewById(R.id.pune_text);
        mumbai_text = findViewById(R.id.mumbai_text);
        delhi_text = findViewById(R.id.delhi_text);
        chennai_text = findViewById(R.id.chennai_text);
        lucknow_text = findViewById(R.id.lucknow_text);
        jaipur_text = findViewById(R.id.jaipur_text);
        bangalore_text = findViewById(R.id.banglore_text);
        agra_text = findViewById(R.id.agra_text);
        chandigarh_text = findViewById(R.id.chandigarh_text);

        pune.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.buttons);
                pune_text.setTextColor(0xFFFFFFFF);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                spinner.setSelection(465);
                selectedCity = "Pune";
            }
        });

        mumbai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.buttons);
                mumbai_text.setTextColor(0xFFFFFFFF);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(392);
                selectedCity = "Mumbai";
            }
        });

        delhi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.buttons);
                delhi_text.setTextColor(0XFFFFFFFF);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(145);
                selectedCity = "Delhi";
            }
        });

        chennai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.buttons);
                chennai_text.setTextColor(0xFFFFFFFF);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(113);
                selectedCity = "Chennai";
            }
        });

        lucknow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.buttons);
                lucknow_text.setTextColor(0xFFFFFFFF);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(355);
                selectedCity = "Lucknow";
            }
        });

        jaipur.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.buttons);
                jaipur_text.setTextColor(0xFFFFFFFF);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(245);
                selectedCity = "Jaipur";
            }
        });

        bangalore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.buttons);
                bangalore_text.setTextColor(0xFFFFFFFF);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(49);
                selectedCity = "Bangalore";
            }
        });

        agra.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.buttons);
                agra_text.setTextColor(0xFFFFFFFF);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                spinner.setSelection(2);
                selectedCity = "Agra";
            }
        });

        chandigarh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.buttons);
                chandigarh_text.setTextColor(0xFFFFFFFF);
                spinner.setSelection(109);
                selectedCity = "Chandigarh";
            }
        });
    }

    public void addCity()
    {
        String[] allCitys = {"Adilabad","Agra","Ahmedabad","Ahmednagar","Aizawl","Ajitgarh (Mohali)","Ajmer","Akola","Alappuzha","Aligarh",
                "Alirajpur","Allahabad","Almora","Alwar","Ambala","Ambedkar Nagar","Amravati","Amreli district","Amritsar","Anand",
                "Anantapur","Anantnag","Angul","Anjaw","Anuppur","Araria","Ariyalur","Arwal","Ashok Nagar","Auraiya",
                "Aurangabad","Aurangabad","Azamgarh","Badgam","Bagalkot","Bageshwar","Bagpat","Bahraich","Baksa","Balaghat",
                "Balangir","Balasore","Ballia","Balrampur","Banaskantha","Banda","Bandipora","Bangalore Rural","Bangalore Urban","Banka",
                "Bankura","Banswara","Barabanki","Baramulla","Baran","Bardhaman","Bareilly","Bargarh (Baragarh)","Barmer","Barnala",
                "Barpeta","Barwani","Bastar","Basti","Bathinda","Beed","Begusarai","Belgaum","Bellary","Betul",
                "Bhadrak","Bhagalpur","Bhandara","Bharatpur","Bharuch","Bhavnagar","Bhilwara","Bhind","Bhiwani","Bhojpur",
                "Bhopal","Bidar","Bijapur","Bijapur","Bijnor","Bikaner","Bilaspur","Bilaspur","Birbhum","Bishnupur",
                "Bokaro","Bongaigaon","Boudh (Bauda)","Budaun","Bulandshahr","Buldhana","Bundi","Burhanpur","Buxar","Cachar",
                "Central Delhi","Chamarajnagar","Chamba","Chamoli","Champawat","Champhai","Chandauli","Chandel","Chandigarh","Chandrapur",
                "Changlang","Chatra","Chennai","Chhatarpur","Chhatrapati Shahuji Maharaj Nagar","Chhindwara","Chikkaballapur","Chikkamagaluru","Chirang","Chitradurga",
                "Chitrakoot","Chittoor","Chittorgarh","Churachandpur","Churu","Coimbatore","Cooch Behar","Cuddalore","Cuttack","Dadra and Nagar Haveli",
                "Dahod","Dakshin Dinajpur","Dakshina Kannada","Daman","Damoh","Dantewada","Darbhanga","Darjeeling","Darrang","Datia",
                "Dausa","Davanagere","Debagarh (Deogarh)","Dehradun","Delhi","Deoghar","Deoria","Dewas","Dhalai","Dhamtari","Dhanbad",
                "Dhar","Dharmapuri","Dharwad","Dhemaji","Dhenkanal","Dholpur","Dhubri","Dhule","Dibang Valley","Dibrugarh",
                "Dima Hasao","Dimapur","Dindigul","Dindori","Diu","Doda","Dumka","Dungapur","Durg","East Champaran",
                "East Delhi","East Garo Hills","East Khasi Hills","East Siang","East Sikkim","East Singhbhum","Eluru","Ernakulam","Erode","Etah",
                "Etawah","Faizabad","Faridabad","Faridkot","Farrukhabad","Fatehabad","Fatehgarh Sahib","Fatehpur","Fazilka","Firozabad",
                "Firozpur","Gadag","Gadchiroli","Gajapati","Ganderbal","Gandhinagar","Ganganagar","Ganjam","Garhwa","Gautam Buddh Nagar",
                "Gaya","Ghaziabad","Ghazipur","Giridih","Goalpara","Godda","Golaghat","Gonda","Gondia","Gopalganj",
                "Gorakhpur","Gulbarga","Gumla","Guna","Guntur","Gurdaspur","Gurgaon","Gwalior","Hailakandi","Hamirpur",
                "Hamirpur","Hanumangarh","Harda","Hardoi","Haridwar","Hassan","Haveri district","Hazaribag","Hingoli","Hissar",
                "Hooghly","Hoshangabad","Hoshiarpur","Howrah","Hyderabad","Hyderabad","Idukki","Imphal East","Imphal West","Indore",
                "Jabalpur","Jagatsinghpur","Jaintia Hills","Jaipur","Jaisalmer","Jajpur","Jalandhar","Jalaun","Jalgaon","Jalna",
                "Jalore","Jalpaiguri","Jammu","Jamnagar","Jamtara","Jamui","Janjgir Champa","Jashpur","Jaunpur district","Jehanabad",
                "Jhabua","Jhajjar","Jhalawar","Jhansi","Jharsuguda","Jhunjhunu","Jind","Jodhpur","Jorhat","Junagadh",
                "Jyotiba Phule Nagar","Kabirdham (formerly Kawardha)","Kadapa","Kaimur","Kaithal","Kakinada","Kalahandi","Kamrup","Kamrup Metropolitan","Kanchipuram",
                "Kandhamal","Kangra","Kanker","Kannauj","Kannur","Kanpur","Kanshi Ram Nagar","Kanyakumari","Kapurthala","Karaikal",
                "Karauli","Karbi Anglong","Kargil","Karimganj","Karimnagar","Karnal","Karur","Kasaragod","Kathua","Katihar",
                "Katni","Kaushambi","Kendrapara","Kendujhar (Keonjhar)","Khagaria","Khammam","Khandwa (East Nimar)","Khargone (West Nimar)","Kheda","Khordha",
                "Khowai","Khunti","Kinnaur","Kishanganj","Kishtwar","Kodagu","Koderma","Kohima","Kokrajhar","Kolar",
                "Kolasib","Kolhapur","Kolkata","Kollam","Koppal","Koraput","Korba","Koriya","Kota","Kottayam",
                "Kozhikode","Krishna","Kulgam","Kullu","Kupwara","Kurnool","Kurukshetra","Kurung Kumey","Kushinagar","Kutch",
                "Lahaul and Spiti","Lakhimpur","Lakhimpur Kheri","Lakhisarai","Lalitpur","Latehar","Latur","Lawngtlai","Leh","Lohardaga",
                "Lohit","Lower Dibang Valley","Lower Subansiri","Lucknow","Ludhiana","Lunglei","Madhepura","Madhubani","Madurai","Mahamaya Nagar",
                "Maharajganj","Mahasamund","Mahbubnagar","Mahe","Mahendragarh","Mahoba","Mainpuri","Malappuram","Maldah","Malkangiri",
                "Mamit","Mandi","Mandla","Mandsaur","Mandya","Mansa","Marigaon","Mathura","Mau","Mayurbhanj",
                "Medak","Meerut","Mehsana","Mewat","Mirzapur","Moga","Mokokchung","Mon","Moradabad","Morena",
                "Mumbai City","Mumbai suburban","Munger","Murshidabad","Muzaffarnagar","Muzaffarpur","Mysore","Nabarangpur","Nadia","Nagaon",
                "Nagapattinam","Nagaur","Nagpur","Nainital","Nalanda","Nalbari","Nalgonda","Namakkal","Nanded","Nandurbar",
                "Narayanpur","Narmada","Narsinghpur","Nashik","Navsari","Nawada","Nawanshahr","Nayagarh","Neemuch","Nellore",
                "New Delhi","Nilgiris","Nizamabad","North 24 Parganas","North Delhi","North East Delhi","North Goa","North Sikkim","North Tripura","North West Delhi",
                "Nuapada","Ongole","Osmanabad","Pakur","Palakkad","Palamu","Pali","Palwal","Panchkula","Panchmahal",
                "Panchsheel Nagar district (Hapur)","Panipat","Panna","Papum Pare","Parbhani","Paschim Medinipur","Patan","Pathanamthitta","Pathankot","Patiala",
                "Patna","Pauri Garhwal","Perambalur","Phek","Pilibhit","Pithoragarh","Pondicherry","Poonch","Porbandar","Pratapgarh",
                "Pratapgarh","Pudukkottai","Pulwama","Pune","Purba Medinipur","Puri","Purnia","Purulia","Raebareli","Raichur",
                "Raigad","Raigarh","Raipur","Raisen","Rajauri","Rajgarh","Rajkot","Rajnandgaon","Rajsamand","Ramabai Nagar (Kanpur Dehat)",
                "Ramanagara","Ramanathapuram","Ramban","Ramgarh","Rampur","Ranchi","Ratlam","Ratnagiri","Rayagada","Reasi",
                "Rewa","Rewari","Ri Bhoi","Rohtak","Rohtas","Rudraprayag","Rupnagar","Sabarkantha","Sagar","Saharanpur",
                "Saharsa","Sahibganj","Saiha","Salem","Samastipur","Samba","Sambalpur","Sangli","Sangrur","Sant Kabir Nagar",
                "Sant Ravidas Nagar","Saran","Satara","Satna","Sawai Madhopur","Sehore","Senapati","Seoni","Seraikela Kharsawan","Serchhip",
                "Shahdol","Shahjahanpur","Shajapur","Shamli","Sheikhpura","Sheohar","Sheopur","Shimla","Shimoga","Shivpuri",
                "Shopian","Shravasti","Sibsagar","Siddharthnagar","Sidhi","Sikar","Simdega","Sindhudurg","Singrauli","Sirmaur",
                "Sirohi","Sirsa","Sitamarhi","Sitapur","Sivaganga","Siwan","Solan","Solapur","Sonbhadra","Sonipat",
                "Sonitpur","South 24 Parganas","South Delhi","South Garo Hills","South Goa","South Sikkim","South Tripura","South West Delhi","Sri Muktsar Sahib","Srikakulam",
                "Srinagar","Subarnapur (Sonepur)","Sultanpur","Sundergarh","Supaul","Surat","Surendranagar","Surguja","Tamenglong","Tarn Taran",
                "Tawang","Tehri Garhwal","Thane","Thanjavur","The Dangs","Theni","Thiruvananthapuram","Thoothukudi","Thoubal","Thrissur",
                "Tikamgarh","Tinsukia","Tirap","Tiruchirappalli","Tirunelveli","Tirupur","Tiruvallur","Tiruvannamalai","Tiruvarur","Tonk",
                "Tuensang","Tumkur","Udaipur","Udalguri","Udham Singh Nagar","Udhampur","Udupi","Ujjain","Ukhrul","Umaria",
                "Una","Unnao","Upper Siang","Upper Subansiri","Uttar Dinajpur","Uttara Kannada","Uttarkashi","Vadodara","Vaishali","Valsad",
                "Varanasi","Vellore","Vidisha","Viluppuram","Virudhunagar","Visakhapatnam","Vizianagaram","Vyara","Warangal","Wardha",
                "Washim","Wayanad","West-Champaran","West Delhi","West Garo Hills","West Kameng","West Khasi Hills","West Siang","West Sikkim","West Singhbhum",
                "West Tripura","Wokha","Yadgir","Yamuna-Nagar","Yanam","Yavatmal","Zunheboto",
        };

        for (int i=0;i<638;i++)
        {
            cityList.add(allCitys[i]);
        }
    }
}
//  <<,