@DataJpaTest
@Transactional
@ContextConfiguration(classes = MainApplication.class)
@Import(AdopterService.class)
class AdopterServiceTest {

    @Autowired
    private AdopterService adopterService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private AdopterEntity adopter;

    @BeforeEach
    void setUp() {
        adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);
    }

    @Test
    void testGetAdopter() throws EntityNotFoundException {
        AdopterEntity result = adopterService.getAdopter(adopter.getId());
        assertNotNull(result);
    }

    @Test
    void testGetInvalidAdopter() {
        assertThrows(EntityNotFoundException.class, () -> {
            adopterService.getAdopter(0L);
        });
    }

    @Test
    void testDeleteAdopterWithRequests() {

        AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
        request.setAdopter(adopter);
        entityManager.persist(request);

        assertThrows(IllegalOperationException.class, () -> {
            adopterService.deleteAdopter(adopter.getId());
        });
    }
}
