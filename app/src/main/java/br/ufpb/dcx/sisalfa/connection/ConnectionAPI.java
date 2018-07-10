package br.ufpb.dcx.sisalfa.connection;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import br.ufpb.dcx.sisalfa.database.SisalfaRepository;
import br.ufpb.dcx.sisalfa.models.Challenge;
import br.ufpb.dcx.sisalfa.models.SisContext;
import br.ufpb.dcx.sisalfa.models.User;
import br.ufpb.dcx.sisalfa.util.AndroidUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionAPI {

    private SisalfaRepository sisalfaRepository;

    public ConnectionAPI(Context ctx){
        this.sisalfaRepository =  new SisalfaRepository(ctx);
    }

    private Retrofit retrofitBuilder(String baseURL){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    public void startContexts() {
        Retrofit retrofit = retrofitBuilder(AndroidUtils.CONTEXTS_BASE_URL);
        LiteracyAPI literacyAPI = retrofit.create(LiteracyAPI.class);

        Call<List<SisContext>> call = literacyAPI.getAllContexts();
        call.enqueue(new Callback<List<SisContext>>() {
            @Override
            public void onResponse(Call<List<SisContext>> call, Response<List<SisContext>> response) {
                if(response.isSuccessful()) {
                    List<SisContext> changesList = response.body();
                    for(SisContext sc: changesList){
                        System.out.println("Nome: "+ sc.getName());
                        if(sisalfaRepository.getAllContexts().contains(sc)){
                            Log.i("TAG", "Object already exist.");
                        }
                        else{
                            sisalfaRepository.createContext(sc);
                        }
                    }

                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<SisContext>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void startUsers() {
        Retrofit retrofit = retrofitBuilder(AndroidUtils.USERS_BASE_URL);
        LiteracyAPI literacyAPI = retrofit.create(LiteracyAPI.class);

        Call<List<User>> call = literacyAPI.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> changesList = response.body();
                    for(User user: changesList){
                        if(sisalfaRepository.getAllContexts().contains(user)){
                            Log.i("TAG", "Object already exist.");
                        }
                        else{
                            sisalfaRepository.createUser(user);
                        }
                    }
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    public void startChallenges() {
        Retrofit retrofit = retrofitBuilder(AndroidUtils.CHALLENGES_BASE_URL);
        LiteracyAPI literacyAPI = retrofit.create(LiteracyAPI.class);

        Call<List<Challenge>> call = literacyAPI.getAllChallenges();
        call.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if(response.isSuccessful()) {
                    List<Challenge> changesList = response.body();
                    for(Challenge c: changesList){
                        if(sisalfaRepository.getAllContexts().contains(c)){
                            Log.i("TAG", "Object already exist.");
                        }
                        else{
                            sisalfaRepository.createChallenge(c);
                        }
                    }
                }else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
